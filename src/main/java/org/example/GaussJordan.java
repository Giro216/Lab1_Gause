package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GaussJordan {
    public GaussJordan() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/main/resources/input.txt"));
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        Fraction[][] matrix = new Fraction[m][n + 1];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j <= n; j++) {
                matrix[i][j] = new Fraction(scanner.nextInt(), 1);
            }
        }

        printMatrix(matrix);

        solve(matrix);
    }

    private static void solve(Fraction[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length - 1;

        // Поиск максимального элемента в строке
        for (int cur_col = 0; cur_col < n; cur_col++) {
            int maxRow = cur_col;
            for (int row = cur_col + 1; row < m; row++) {
                if (matrix[row][cur_col].numerator * matrix[maxRow][cur_col].denominator >
                        matrix[maxRow][cur_col].numerator * matrix[row][cur_col].denominator) {
                    maxRow = row;
                }
            }

            if (cur_col == m){
                break;
            }

            Fraction[] temp = matrix[cur_col];
            matrix[cur_col] = matrix[maxRow];
            matrix[maxRow] = temp;

            if (matrix[cur_col][cur_col].numerator == 0) {
                continue;
            }

            // Вычитание
            Fraction one = new Fraction(1,1);
            Fraction factor = one.divide(matrix[cur_col][cur_col]);
            for (int k = cur_col; k <= n; k++) {
                matrix[cur_col][k] = matrix[cur_col][k].multiply(factor);
            }
            for (int row = 0; row < m; row++) {
                if (row != cur_col) {
                    factor = matrix[row][cur_col].divide(matrix[cur_col][cur_col]);
                    for (int k = cur_col; k <= n; k++) {
                        matrix[row][k] = matrix[row][k].subtract(matrix[cur_col][k].multiply(factor));
                    }
                }
            }

            printMatrix(matrix);
        }

        for (int row = 0; row < m; row++) {
            if (matrix[row][row].numerator != 0) {
                matrix[row][n] = matrix[row][n].divide(matrix[row][row]);
                matrix[row][row] = new Fraction(1, 1);
            }
        }

        printSolution(matrix);
    }

    private static void printMatrix(Fraction[][] matrix) {
        for (Fraction[] row : matrix) {
            for (Fraction elem : row) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printSolution(Fraction[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length - 1;

        for (int row = 0; row < m; row++) {
            boolean allZero = true;
            for (int col = 0; col < n; col++) {
                if (matrix[row][col].numerator != 0) {
                    allZero = false;
                    break;
                }
            }
            if (allZero && matrix[row][n].numerator != 0) {
                System.out.println("The system has no solution.");
                return;
            }
        }

        for (int row = 0; row < m; row++) {
            boolean allZero = true;
            for (int col = 0; col < n; col++) {
                if (matrix[row][col].numerator != 0) {
                    allZero = false;
                    break;
                }
            }
            if (allZero) {
                System.out.println("The system has infinitely many solutions.");
                return;
            }
        }

        System.out.println("The system has a unique solution:");
        for (int row = 0; row < m; row++) {
            System.out.println("x" + (row + 1) + " = " + matrix[row][n]);
        }
    }
}
