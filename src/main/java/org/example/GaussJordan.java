package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class GaussJordan {
    public GaussJordan() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/main/resources/input.txt"));
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        Fraction[][] matrix = new Fraction[m][n + 1];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j <= n; j++) {
                matrix[i][j] = new Fraction(scanner.nextLong(), 1);
            }
        }

        printMatrix(matrix);

        solve(matrix);
    }

    private static void solve(Fraction[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length - 1;

        int pivotRow = 0; // Текущая строка для поиска ведущего элемента
        int pivotCol = 0; // Текущий столбец для поиска ведущего элемента
        boolean lead_zero = false;

        while (pivotRow < m && pivotCol < n) {

            // Поиск максимального элемента в текущем столбце, начиная с pivotRow
            int maxRow = pivotRow;
            for (int row = (lead_zero) ? pivotRow : pivotRow + 1; row < m; row++) {
                // Приводим дроби к общему знаменателю и сравниваем абсолютные значения числителей
                long a = Math.abs(matrix[row][pivotCol].numerator * matrix[maxRow][pivotCol].denominator);
                long b = Math.abs(matrix[maxRow][pivotCol].numerator * matrix[row][pivotCol].denominator);
                if (a > b) {
                    maxRow = row;
                }
            }

            // Если все элементы в столбце нулевые, переходим к следующему столбцу
            if (matrix[maxRow][pivotCol].numerator == 0) {
                pivotCol++;
                lead_zero = true;
                continue;
            }

            // Меняем строки местами, если необходимо
            if (maxRow != pivotRow) {
                Fraction[] temp = matrix[pivotRow];
                matrix[pivotRow] = matrix[maxRow];
                matrix[maxRow] = temp;
            }

            // Нормализация текущей строки
            Fraction one = new Fraction(1, 1);
            Fraction factor = one.divide(matrix[pivotRow][pivotCol]);
            for (int k = pivotCol; k <= n; k++) {
                matrix[pivotRow][k] = matrix[pivotRow][k].multiply(factor);
            }

            // Исключение элементов в текущем столбце для всех строк, кроме текущей
            for (int row = 0; row < m; row++) {
                if (row != pivotRow) {
                    factor = matrix[row][pivotCol].divide(matrix[pivotRow][pivotCol]);
                    for (int k = pivotCol; k <= n; k++) {
                        matrix[row][k] = matrix[row][k].subtract(matrix[pivotRow][k].multiply(factor));
                    }
                }
            }

            // Переход к следующей строке и столбцу
            pivotRow++;
            pivotCol++;

            // Вывод промежуточной матрицы
            printMatrix(matrix);
        }

        // Приведение матрицы к упрощенному виду
        for (int row = 0; row < m; row++) {
            if (matrix[row][row].numerator != 0) {
                matrix[row][n] = matrix[row][n].divide(matrix[row][row]);
                matrix[row][row] = new Fraction(1, 1);
            }
        }

        // Вывод решения
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
                PrintSystemSolution(matrix);
                return;
            }
        }

        System.out.println("The system has a unique solution:");
        for (int row = 0; row < m; row++) {
            System.out.println("x" + (row + 1) + " = " + matrix[row][n]);
        }
    }

    private static void PrintSystemSolution(Fraction[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int row = 0; row < rows; row++) {
            ArrayList<Fraction> coefficients = new ArrayList<>(cols);
            int num_index = -1;
            for (int col = 0; col < cols; col++) {
                coefficients.add(matrix[row][col]);
                if (matrix[row][col].equals(1.f) && num_index == -1) num_index = col;
            }

            if (num_index != -1) {
                System.out.print("X" + (num_index + 1) + " = ");
                for (int index = 0; index < coefficients.size(); index++) {
                    if (index != num_index && !coefficients.get(index).toString().equals("0")) {
                        if (index != coefficients.size() - 1){
                            coefficients.get(index).numerator *= -1;
                            System.out.print(coefficients.get(index).toString() + " * X" + (index + 1) + " + ");
                        }else{
                            System.out.println(coefficients.get(index).toString());
                        }
                    }else if (coefficients.get(index).toString().equals("0") && index == coefficients.size() - 1) {
                        System.out.println(coefficients.get(index).toString());
                    }
                }
            }

        }
    }
}