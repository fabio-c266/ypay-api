package com.ypay.api.validations;

public class CNPJValidation {
    public static boolean isValid(String cnpj) {
        int[] firstBaseCnpjNumbersMultipliers = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] secondBaseCnpjNumbersMultipliers = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        String firstTwelveNumbersOfCnpj = cnpj.substring(0, 12);
        int actualFirstVerifier = Character.getNumericValue(cnpj.charAt(12));
        int actualSecondVerifier = Character.getNumericValue(cnpj.charAt(13));

        int firstCalculation = calculateCnpjBaseNumbers(firstBaseCnpjNumbersMultipliers, firstTwelveNumbersOfCnpj);
        int realFirstVerifier = getRealVerifierNumber(firstCalculation);

        if (realFirstVerifier != actualFirstVerifier) {
            return false;
        }

        String firstThirteenNumbersOfCnpj = cnpj.substring(0, 13);
        int secondCalculation = calculateCnpjBaseNumbers(secondBaseCnpjNumbersMultipliers, firstThirteenNumbersOfCnpj);
        int realSecondVerifier = getRealVerifierNumber(secondCalculation);
        return (actualSecondVerifier == realSecondVerifier);
    }

    private static int calculateCnpjBaseNumbers(int[] multipliers, String cnpjBase) {
        int sum = 0;
        for (int i = 0; i < cnpjBase.length(); i++) {
            int number = Character.getNumericValue(cnpjBase.charAt(i));
            sum += number * multipliers[i];
        }
        return sum;
    }

    private static int getRealVerifierNumber(int sum) {
        int remainder = sum % 11;
        return (remainder < 2) ? 0 : 11 - remainder;
    }
}
