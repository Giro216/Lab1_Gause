package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

abstract class LinearSystemSolver {
    protected boolean FindBasic;
    protected Fraction[][] matrix;

    public LinearSystemSolver(boolean FindBasic) throws FileNotFoundException {
        this.FindBasic = FindBasic;
        Scanner scanner = new Scanner(new File("src/main/resources/input.txt"));
        int m = scanner.nextInt();
        int n = scanner.nextInt();
        matrix = new Fraction[m][n + 1];

        // Считывание данных в виде строк для поддержки дробных значений
        for (int i = 0; i < m; i++) {
            for (int j = 0; j <= n; j++) {
                String token = scanner.next();
                matrix[i][j] = Fraction.parse(token);
            }
        }

        printMatrix(matrix);
    }

    // Перегруженный конструктор для передачи матрицы напрямую
    public LinearSystemSolver(Fraction[][] matrix1, boolean FindBasic) {
        this.FindBasic = FindBasic;
        matrix = matrix1;
        printMatrix(matrix);
    }

    protected void solve() {
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
            if (FindBasic) printMatrix(matrix);
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

        if (FindBasic) new BasisSolutions(matrix);
    }

    public static void printMatrix(Fraction[][] matrix) {
        for (Fraction[] row : matrix) {
            for (Fraction elem : row) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    protected abstract void printSolution(Fraction[][] matrix);

    protected abstract void printUniqueSolution(Fraction[][] matrix);

    protected abstract void printInfiniteSolutions(Fraction[][] matrix);

    // Поиск ведущего столбца (первого ненулевого элемента)
    public static int findPivotColumn(Fraction[] row, int n) {
        for (int col = 0; col < n; col++) {
            if (row[col].equals(1.f)) {
                return col;
            }
        }
        return -1;
    }

    // Проверка, является ли строка нулевой (исключая последний столбец)
    public static boolean isZeroRow(Fraction[] row, int n) {
        for (int col = 0; col < n; col++) {
            if (!row[col].equals(0.f)) {
                return false;
            }
        }
        return true;
    }
}


public class GaussJordan extends LinearSystemSolver {

    public GaussJordan(boolean FindBasic) throws FileNotFoundException {
        super(FindBasic);
        solve();
    }

    public GaussJordan(Fraction[][] matrix, boolean FindBasic) {
        super(matrix, FindBasic);
        solve();
    }

    @Override
    protected void printSolution(Fraction[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length - 1;

        // Проверка на отсутствие решений
        for (int row = 0; row < m; row++) {
            if (isZeroRow(matrix[row], n) && !matrix[row][n].equals(0.f)) {
                System.out.println("The system has no solution.");
                return;
            }
        }

        // Проверка на бесконечное количество решений
        boolean hasInfiniteSolutions = false;
        for (int row = 0; row < m; row++) {
            if (isZeroRow(matrix[row], n)) {
                hasInfiniteSolutions = true;
                break;
            }
        }

        if (hasInfiniteSolutions) {
            System.out.println("The system has infinitely many solutions.");
            printInfiniteSolutions(matrix);
        } else {
            System.out.println("The system has a unique solution:");
            printUniqueSolution(matrix);
        }
    }

    @Override
    protected void printUniqueSolution(Fraction[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length - 1;

        for (int row = 0; row < m; row++) {
            int index = findPivotColumn(matrix[row], n);
            System.out.println("x" + (index + 1) + " = " + matrix[row][n]);
        }
    }

    @Override
    protected void printInfiniteSolutions(Fraction[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int row = 0; row < rows; row++) {
            int pivotCol = findPivotColumn(matrix[row], cols - 1);
            if (pivotCol != -1) {
                System.out.print("X" + (pivotCol + 1) + " = ");
                for (int col = 0; col < cols - 1; col++) {
                    if (col != pivotCol && !matrix[row][col].equals(0.f)) {
                        Fraction coefficient = matrix[row][col].negate(); // Умножаем на -1
                        System.out.print(coefficient + " * X" + (col + 1) + " + ");
                    }
                }
                System.out.println(matrix[row][cols - 1]); // Свободный член
            }
        }
    }
}

