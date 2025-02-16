package org.example;

class Fraction {
    long numerator;
    long denominator;

    Fraction(long numerator, long denominator) {
        if (denominator == 0) {
            throw new IllegalArgumentException("Denominator cannot be zero.");
        }
        long gcd = gcd(Math.abs(numerator), Math.abs(denominator));
        this.numerator = numerator / gcd;
        this.denominator = denominator / gcd;
        if (this.denominator < 0) {
            this.numerator *= -1;
            this.denominator *= -1;
        }
    }

    // Статический метод для парсинга строки в Fraction.
    public static Fraction parse(String token) {
        token = token.trim();
        if (token.contains("/")) {
            // Формат a/b
            String[] parts = token.split("/");
            long num = Long.parseLong(parts[0]);
            long den = Long.parseLong(parts[1]);
            return new Fraction(num, den);
        } else if (token.contains(".")) {
            // Десятичное число, например "3.14" преобразуем в дробь 314/100
            int index = token.indexOf(".");
            int decimals = token.length() - index - 1;
            long den = (long) Math.pow(10, decimals);
            String normalized = token.replace(".", "");
            long num = Long.parseLong(normalized);
            return new Fraction(num, den);
        } else {
            // Целое число
            return new Fraction(Long.parseLong(token), 1);
        }
    }

    Fraction add(Fraction other) {
        long num = this.numerator * other.denominator + other.numerator * this.denominator;
        long den = this.denominator * other.denominator;
        return new Fraction(num, den);
    }

    Fraction subtract(Fraction other) {
        long num = this.numerator * other.denominator - other.numerator * this.denominator;
        long den = this.denominator * other.denominator;
        return new Fraction(num, den);
    }

    Fraction multiply(Fraction other) {
        long num = this.numerator * other.numerator;
        long den = this.denominator * other.denominator;
        return new Fraction(num, den);
    }

    Fraction divide(Fraction other) {
        if (other.numerator == 0) {
            throw new IllegalArgumentException("Cannot divide by zero.");
        }
        long num = this.numerator * other.denominator;
        long den = this.denominator * other.numerator;
        return new Fraction(num, den);
    }

    public boolean equals(double num) {
        return (double)this.numerator / this.denominator == num;
    }

    @Override
    public String toString() {
        if (denominator == 1) {
            return Long.toString(numerator);
        }
        return numerator + "/" + denominator;
    }

    private long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public Fraction negate() {
        this.numerator *= -1;
        return this;
    }
}
