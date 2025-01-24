package org.example.readnigstore.Helper;

public class Validators {
    public static boolean validateCardDetails(String cardNumber, String expiryDate, String cvv) {
        if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            return false;
        }

        if (cvv.length() != 3 || !cvv.matches("\\d+")) {
            return false;
        }

        // Validate expiry date format (MM/YY) and check if it's in the future
        try {
            String[] parts = expiryDate.split("/");
            if (parts.length != 2) return false;
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000; // Adjust for 2000s
            if (month < 1 || month > 12 || year < 2024) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
