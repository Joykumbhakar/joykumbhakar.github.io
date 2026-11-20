package com.securething.vault

import org.junit.Assert.assertEquals
import org.junit.Test

class VaultUnitTest {

    @Test
    fun testFormatCardNumber_standard16Digits() {
        val input = "1234567812345678"
        val expected = "1234 5678 1234 5678"
        assertEquals(expected, formatCardNumber(input))
    }

    @Test
    fun testFormatCardNumber_withPreExistingSpaces() {
        val input = "1234 5678 1234 5678"
        val expected = "1234 5678 1234 5678"
        assertEquals(expected, formatCardNumber(input))
    }

    @Test
    fun testFormatCardNumber_withRandomSpacesAndPadding() {
        val input = " 1234  5678 1234 5678 "
        val expected = "1234 5678 1234 5678"
        assertEquals(expected, formatCardNumber(input))
    }

    @Test
    fun testFormatIdNumber_aadhaar() {
        val input = "123456789012"
        val expected = "1234 5678 9012"
        assertEquals(expected, formatIdNumber(input, "aadhaar"))
    }

    @Test
    fun testFormatIdNumber_aadhaarWithSpaces() {
        val input = " 1234 5678 9012 "
        val expected = "1234 5678 9012"
        assertEquals(expected, formatIdNumber(input, "aadhaar"))
    }

    @Test
    fun testFormatIdNumber_pan() {
        val input = "abcde1234f"
        val expected = "ABCDE1234F"
        assertEquals(expected, formatIdNumber(input, "pan"))
    }

    @Test
    fun testFormatIdNumber_voter() {
        val input = "xyz9876543"
        val expected = "XYZ9876543"
        assertEquals(expected, formatIdNumber(input, "voter"))
    }

    @Test
    fun testCalculatePasswordStrength() {
        assertEquals(0, calculatePasswordStrength(""))
        assertEquals(0, calculatePasswordStrength("abc"))
        // length >= 6
        assertEquals(1, calculatePasswordStrength("abcdef"))
        // length >= 8
        assertEquals(2, calculatePasswordStrength("abcdefgh"))
        // length >= 8 + letter and digit
        assertEquals(3, calculatePasswordStrength("abcdefg8"))
        // length >= 8 + letter and digit + special char
        assertEquals(4, calculatePasswordStrength("Abcdef8!"))
    }

    @Test
    fun testGenerateRandomPassword_defaultLength() {
        val pwd = generateRandomPassword()
        assertEquals(16, pwd.length)
        org.junit.Assert.assertTrue(pwd.isNotEmpty())
    }

    @Test
    fun testGenerateRandomPassword_customLength() {
        val pwd24 = generateRandomPassword(24)
        assertEquals(24, pwd24.length)
    }

    @Test
    fun testFormatStorageSize() {
        val kbStr = formatStorageSize(500_000L)
        org.junit.Assert.assertTrue(kbStr.contains("KB"))

        val mbStr = formatStorageSize(50_000_000L)
        org.junit.Assert.assertTrue(mbStr.contains("MB"))

        val gbStr = formatStorageSize(2_500_000_000L)
        org.junit.Assert.assertTrue(gbStr.contains("GB"))
    }

    @Test
    fun testNC_accentColors() {
        assertEquals(NC.Cyan, NC.accentFor("photo"))
        assertEquals(NC.Violet, NC.accentFor("video"))
        assertEquals(NC.Green, NC.accentFor("audio"))
        assertEquals(NC.Amber, NC.accentFor("file"))
    }
}
