package org.example;

import java.util.*;

public class BasisSolutions {
    private Fraction[][] matrix;
    private Fraction[][] basicMatrix;
    private int rows;
    private final int cols;
    private final List<List<Fraction>> Solutions;

    BasisSolutions(Fraction[][] matrix) {
        this.matrix = matrix;
        this.rows = matrix.length;
        this.cols = matrix[0].length - 1;
        this.Solutions = new ArrayList<>();

        RefactorMatrix();
        FindBasis();
    }

    private void FindBasis() {
        for (int i = 0; i < cols; i++) {
            for (int j = i + 1; j < cols; j++) {
                MakeBasicMatrix(i, j);
                System.out.println("\nBasis of X" + (i+1) + " and X" + (j+1) + " in matrix");
                if (CheckLinearDependence()){
                    System.out.println("System has linear dependence. It is a no solution.");
                }else{
                    GaussJordanBasis BasicMatrixSolve = new GaussJordanBasis(basicMatrix, false);
                    Solutions.add(BasicMatrixSolve.getSolution());
                }
            }
        }
        PrintResult();
    }

    private boolean CheckLinearDependence() {
        for (int i = 0; i < rows; i++) {
            if (LinearSystemSolver.isZeroRow(basicMatrix[i], cols)) {
                return true;
            }
        }
        return false;
    }

    private void PrintResult() {
        System.out.println("\nBasises:");
        for (List<Fraction> result: Solutions ) {
            System.out.print("(");
            for (Fraction fraction: result) {
                System.out.print(fraction.toString() + " ");
            }
            System.out.println("\b)");
        }
    }

    private void MakeBasicMatrix(int row, int col) {
        Fraction ZeroElement = new Fraction(0, 1);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols + 1; j++) {
                if (j == row || j == col || (j == cols)) {
                    basicMatrix[i][j] = matrix[i][j];
                }else{
                    basicMatrix[i][j] = ZeroElement;
                }
            }
        }
    }

    private void RefactorMatrix() {
        for (int i = 0; i < rows; i++) {
            if (GaussJordan.isZeroRow(matrix[i], cols)) {
                RemoveRow(i);
                continue;
            }
        }
        this.basicMatrix = new Fraction[rows][cols + 1];
    }

    private void RemoveRow(int rowToRemove) {
        Fraction[][] newMatrix = new Fraction[rows - 1][cols];

        if (rowToRemove >= 0)
            System.arraycopy(matrix, 0, newMatrix, 0, rowToRemove);

        if (rows - (rowToRemove + 1) >= 0)
            System.arraycopy(matrix, rowToRemove + 1, newMatrix, rowToRemove + 1 - 1, rows - (rowToRemove + 1));

        this.matrix = newMatrix;
        this.rows--;
    }
}
