package org.example;

import java.io.FileNotFoundException;
import java.util.*;

public class GaussJordanBasis extends LinearSystemSolver{
    private List<Fraction> Solution;

    public GaussJordanBasis(boolean FindBasic) throws FileNotFoundException {
        super(FindBasic);
        Solution = new ArrayList<>(Collections.nCopies(matrix[0].length-1, new Fraction(0, 1)));
        solve();
    }

    public GaussJordanBasis(Fraction[][] matrix1, boolean FindBasic) {
        super(matrix1, FindBasic);
        Solution = new ArrayList<>(Collections.nCopies(matrix[0].length-1, new Fraction(0, 1)));
        solve();
    }

    public List<Fraction> getSolution() {
        return Solution;
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
            Solution.set(index, matrix[row][n]);
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
                        Fraction coefficient = matrix[row][col].negate();
                        System.out.print(coefficient + " * X" + (col + 1) + " + ");
                    }
                }
                System.out.println(matrix[row][cols - 1]);
            }
        }
    }
}
