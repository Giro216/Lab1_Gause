package org.example;

import java.io.FileNotFoundException;
import java.util.*;

public class GaussJordanBasis extends LinearSystemSolver{
    private final List<Fraction> Solution;

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
    protected void printSolution() {
        int n = matrix[0].length - 1;

        // Проверка на отсутствие решений
        for (Fraction[] value : matrix) {
            if (isZeroRow(value, n) && !value[n].equals(0.f)) {
                System.out.println("The system has no solution.");
                return;
            }
        }

        // Проверка на бесконечное количество решений
        boolean hasInfiniteSolutions = false;
        for (Fraction[] fractions : matrix) {
            if (isZeroRow(fractions, n)) {
                hasInfiniteSolutions = true;
                break;
            }
        }

        if (hasInfiniteSolutions) {
            System.out.println("The system has infinitely many solutions.");
            printInfiniteSolutions();
        } else {
            System.out.println("The system has a unique solution:");
            printUniqueSolution();
        }
    }

    @Override
    protected void printUniqueSolution() {
        int n = matrix[0].length - 1;

        for (Fraction[] fractions : matrix) {
            int index = findPivotColumn(fractions, n);
            Solution.set(index, fractions[n]);
            System.out.println("x" + (index + 1) + " = " + fractions[n]);
        }
    }

    @Override
    protected void printInfiniteSolutions() {
        int cols = matrix[0].length;

        for (Fraction[] fractions : matrix) {
            int pivotCol = findPivotColumn(fractions, cols - 1);
            if (pivotCol != -1) {
                System.out.print("X" + (pivotCol + 1) + " = ");
                for (int col = 0; col < cols - 1; col++) {
                    if (col != pivotCol && !fractions[col].equals(0.f)) {
                        Fraction coefficient = fractions[col].negate();
                        System.out.print(coefficient + " * X" + (col + 1) + " + ");
                    }
                }
                System.out.println(fractions[cols - 1]);
            }
        }
    }
}
