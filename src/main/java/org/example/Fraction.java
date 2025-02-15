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
        double den = this.numerator * this.denominator;
        return den == num;
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
}