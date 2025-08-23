package com.example.qrcraft.data

import com.google.mlkit.vision.barcode.common.Barcode

sealed class QrContentType {
    object Text : QrContentType()
    object Link : QrContentType()
    object Contact : QrContentType()
    object Phone : QrContentType()
    object GeoLocation : QrContentType()
    object WiFi : QrContentType()
    object Unknown : QrContentType()
}

data class QrContent(
    val type: QrContentType,
    val value: String,
    val displayName: String = getDisplayName(type),
    val formattedData: Map<String, String> = emptyMap()
)

private fun getDisplayName(type: QrContentType): String {
    return when (type) {
        QrContentType.Text -> "Text"
        QrContentType.Link -> "Link"
        QrContentType.Contact -> "Contact"
        QrContentType.Phone -> "Phone Number"
        QrContentType.GeoLocation -> "Geolocation"
        QrContentType.WiFi -> "Wi-Fi"
        QrContentType.Unknown -> "Unknown"
    }
}

object QrContentDetector {
    fun detectContentType(barcode: Barcode): QrContent {
        val rawValue = barcode.rawValue ?: ""

        return when (barcode.valueType) {
            Barcode.TYPE_URL -> {
                val url = barcode.url
                val cleanUrl = cleanUrl(url?.url ?: rawValue)
                QrContent(
                    type = QrContentType.Link,
                    value = rawValue,
                    formattedData = mapOf(
                        "URL" to cleanUrl,
                        "Title" to (url?.title ?: "")
                    ).filter { it.value.isNotEmpty() }
                )
            }

            Barcode.TYPE_PHONE -> {
                val phone = barcode.phone
                QrContent(
                    type = QrContentType.Phone,
                    value = rawValue,
                    formattedData = mapOf(
                        "Number" to (phone?.number ?: rawValue),
                        "Type" to getPhoneType(phone?.type)
                    ).filter { it.value.isNotEmpty() }
                )
            }

            Barcode.TYPE_WIFI -> {
                val wifi = barcode.wifi
                QrContent(
                    type = QrContentType.WiFi,
                    value = rawValue,
                    formattedData = mapOf(
                        "SSID" to (wifi?.ssid ?: ""),
                        "Password" to (wifi?.password ?: ""),
                        "Encryption Type" to getWifiEncryptionType(wifi?.encryptionType)
                    ).filter { it.value.isNotEmpty() }
                )
            }

            Barcode.TYPE_GEO -> {
                val geoPoint = barcode.geoPoint
                QrContent(
                    type = QrContentType.GeoLocation,
                    value = rawValue,
                    formattedData = if (geoPoint != null) {
                        mapOf(
                            "" to "${geoPoint.lat}, ${geoPoint.lng}"
                        )
                    } else emptyMap()
                )
            }

            Barcode.TYPE_CONTACT_INFO -> {
                val contactInfo = barcode.contactInfo
                val formattedData = mutableMapOf<String, String>()

                contactInfo?.name?.let { name ->
                    val fullName = "${name.first ?: ""} ${name.last ?: ""}".trim()
                    if (fullName.isNotEmpty()) {
                        formattedData["Name"] = fullName
                    }
                }
                contactInfo?.phones?.firstOrNull()?.number?.let { phone ->
                    formattedData["Phone"] = phone
                }
                contactInfo?.organization?.let { org ->
                    if (org.isNotEmpty()) {
                        formattedData["Organization"] = org
                    }
                }
                contactInfo?.addresses?.firstOrNull()?.let { address ->
                    val addressLines = address.addressLines
                    if (addressLines != null && addressLines.isNotEmpty()) {
                        val cleanAddress = addressLines.joinToString(", ")
                        if (cleanAddress.isNotEmpty()) {
                            formattedData["Address"] = cleanAddress
                        }
                    }
                }

                QrContent(
                    type = QrContentType.Contact,
                    value = rawValue,
                    formattedData = formattedData
                )
            }

            Barcode.TYPE_TEXT -> {
                QrContent(
                    type = QrContentType.Text,
                    value = rawValue
                )
            }

            else -> {
                QrContent(
                    type = QrContentType.Unknown,
                    value = rawValue
                )
            }
        }
    }

    // Temporary bridge function for backwards compatibility
    fun detectContentTypeFromString(rawText: String): QrContent {
        val trimmedText = rawText.trim()

        return when {
            // Link detection
            trimmedText.startsWith("http://", ignoreCase = true) ||
                    trimmedText.startsWith("https://", ignoreCase = true) ||
                    trimmedText.startsWith("www.", ignoreCase = true) -> {
                QrContent(
                    QrContentType.Link, trimmedText, formattedData = mapOf(
                        "URL" to cleanUrl(trimmedText)
                    )
                )
            }

            // Contact detection (vCard)
            trimmedText.startsWith("BEGIN:VCARD", ignoreCase = true) -> {
                // Parse basic contact info from vCard, excluding metadata
                val contactData = parseVCardData(trimmedText)
                QrContent(QrContentType.Contact, trimmedText, formattedData = contactData)
            }

            // WiFi detection
            trimmedText.startsWith("WIFI:", ignoreCase = true) -> {
                val wifiData = parseWifiString(trimmedText)
                QrContent(QrContentType.WiFi, trimmedText, formattedData = wifiData)
            }

            // Tel detection
            trimmedText.startsWith("tel:", ignoreCase = true) -> {
                QrContent(QrContentType.Phone, trimmedText)
            }

            // Phone number detection
            isPhoneNumber(trimmedText) -> {
                QrContent(QrContentType.Phone, trimmedText)
            }

            // GeoLocation detection
            isGeoLocation(trimmedText) -> {
                val geoData = parseGeoLocationData(trimmedText)
                QrContent(QrContentType.GeoLocation, trimmedText, formattedData = geoData)
            }

            // Default to text
            else -> {
                QrContent(QrContentType.Text, trimmedText)
            }
        }
    }

    private fun parseVCardData(vCard: String): Map<String, String> {
        val contactData = mutableMapOf<String, String>()

        vCard.lines().forEach { line ->
            val parts = line.split(":", limit = 2)
            if (parts.size > 1) {
                val key = parts[0].trim().lowercase()
                val value = parts[1].trim()

                // Map vCard fields to user-friendly labels and clean values
                when {
                    key.startsWith("fn") -> {
                        // Full name
                        if (value.isNotEmpty()) {
                            contactData["Name"] = value
                        }
                    }

                    key.startsWith("n") -> {
                        // Name (structured format: Last;First;Middle;Prefix;Suffix)
                        if (value.isNotEmpty() && !contactData.containsKey("Name")) {
                            val nameParts = value.split(";")
                            val firstName = nameParts.getOrNull(1)?.trim() ?: ""
                            val lastName = nameParts.getOrNull(0)?.trim() ?: ""
                            val fullName = "$firstName $lastName".trim()
                            if (fullName.isNotEmpty()) {
                                contactData["Name"] = fullName
                            }
                        }
                    }

                    key.startsWith("tel") -> {
                        // Phone number
                        if (value.isNotEmpty()) {
                            contactData["Phone"] = value
                        }
                    }

                    key.startsWith("email") -> {
                        // Email address
                        if (value.isNotEmpty()) {
                            contactData["Email"] = value
                        }
                    }

                    key.startsWith("org") -> {
                        // Organization
                        if (value.isNotEmpty()) {
                            contactData["Organization"] = value
                        }
                    }

                    key.startsWith("adr") -> {
                        // Address (structured format)
                        if (value.isNotEmpty()) {
                            val addressParts = value.split(";").filter { it.isNotEmpty() }
                            if (addressParts.isNotEmpty()) {
                                contactData["Address"] = addressParts.joinToString(", ")
                            }
                        }
                    }

                    key.startsWith("url") -> {
                        // Website
                        if (value.isNotEmpty()) {
                            contactData["Website"] = value
                        }
                    }
                }
            }
        }

        return contactData
    }

    private fun parseWifiString(wifiString: String): Map<String, String> {
        val wifiData = linkedMapOf<String, String>()

        val content = wifiString.removePrefix("WIFI:").removeSuffix(";;")
        val parts = content.split(";")

        var ssid = ""
        var password = ""
        var encryptionType = "Open"

        for (part in parts) {
            if (part.contains(":")) {
                val keyValue = part.split(":", limit = 2)
                if (keyValue.size == 2) {
                    when (keyValue[0]) {
                        "S" -> {
                            ssid = keyValue[1]
                        }
                        "P" -> {
                            password = keyValue[1]
                        }
                        "T" -> {
                            val security = keyValue[1]
                            encryptionType = when (security.uppercase()) {
                                "WPA" -> "WPA2"
                                "WEP" -> "WEP"
                                "nopass", "" -> "Open"
                                else -> security
                            }
                        }
                    }
                }
            }
        }

        // Add in the desired order
        if (ssid.isNotEmpty()) {
            wifiData["SSID"] = ssid
        }
        if (password.isNotEmpty()) {
            wifiData["Password"] = password
        }
        wifiData["Encryption type"] = encryptionType

        return wifiData
    }

    private fun isPhoneNumber(text: String): Boolean {
        // Remove all whitespace and formatting characters for analysis
        val cleanText = text.replace(Regex("[\\s\\-\\(\\)\\.]"), "")

        return when {
            // International format starting with +
            cleanText.startsWith("+") && cleanText.length >= 8 &&
                    cleanText.substring(1).all { it.isDigit() } -> true

            cleanText.matches(Regex("^1?[0-9]{10}$")) -> true

            cleanText.matches(Regex("^[0-9]{7,15}$")) -> true

            else -> false
        }
    }

    private fun isGeoLocation(text: String): Boolean {
        val geoPatterns = listOf(
            Regex("^-?\\d+\\.?\\d*\\s*,\\s*-?\\d+\\.?\\d*$"), // lat,lng
            Regex("^geo:-?\\d+\\.?\\d*,-?\\d+\\.?\\d*", RegexOption.IGNORE_CASE), // geo:lat,lng
            Regex(
                "^https?://(maps\\.google|www\\.google\\.com/maps|goo\\.gl/maps)",
                RegexOption.IGNORE_CASE
            )
        )
        return geoPatterns.any { it.containsMatchIn(text) }
    }

    private fun getPhoneType(type: Int?): String {
        return when (type) {
            Barcode.Phone.TYPE_WORK -> "Work"
            Barcode.Phone.TYPE_HOME -> "Home"
            Barcode.Phone.TYPE_MOBILE -> "Mobile"
            Barcode.Phone.TYPE_FAX -> "Fax"
            else -> ""
        }
    }

    private fun getWifiEncryptionType(type: Int?): String {
        return when (type) {
            Barcode.WiFi.TYPE_OPEN -> "Open"
            Barcode.WiFi.TYPE_WPA -> "WPA2"
            Barcode.WiFi.TYPE_WEP -> "WEP"
            else -> "Unknown"
        }
    }

    private fun parseGeoLocationData(geoLocation: String): Map<String, String> {
        val geoData = mutableMapOf<String, String>()

        // Remove geo: prefix if present
        val cleanLocation = geoLocation.removePrefix("geo:").removePrefix("GEO:").trim()

        // Extract coordinates from various patterns
        val coordinateRegex = Regex("(-?\\d+\\.?\\d*)\\s*,\\s*(-?\\d+\\.?\\d*)")
        val match = coordinateRegex.find(cleanLocation)

        if (match != null) {
            val (latitude, longitude) = match.destructured
            geoData[""] = "${latitude.trim()}, ${longitude.trim()}"
        }

        return geoData
    }

    private fun cleanUrl(url: String): String {
        val trimmedUrl = url.trim()
        return when {
            trimmedUrl.startsWith("http://https://") || trimmedUrl.startsWith("http://http://") -> {
                // Fix malformed URLs with double protocols
                trimmedUrl.removePrefix("http://")
            }

            trimmedUrl.startsWith("https://") || trimmedUrl.startsWith("http://") -> {
                trimmedUrl
            }

            trimmedUrl.startsWith("www.") -> {
                "https://$trimmedUrl"
            }

            else -> {
                trimmedUrl
            }
        }
    }
}