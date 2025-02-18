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
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            indices.add(i);
        }

        for (List<Integer> basisIndices : getCombinations(indices, rows)) {
            MakeBasicMatrix(basisIndices);
            System.out.println("\nBasis in matrix for columns: " + basisIndices);
            if (CheckLinearDependence()) {
                System.out.println("System has linear dependence. It is a no solution.");
            } else {
                GaussJordanBasis BasicMatrixSolve = new GaussJordanBasis(basicMatrix, false);
                Solutions.add(BasicMatrixSolve.getSolution());
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
        for (List<Fraction> result : Solutions) {
            System.out.print("(");
            for (Fraction fraction : result) {
                System.out.print(fraction.toString() + " ");
            }
            System.out.println("\b)");
        }
    }

    private void MakeBasicMatrix(List<Integer> basisCols) {
        Fraction ZeroElement = new Fraction(0, 1);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols + 1; j++) {
                if (basisCols.contains(j) || j == cols) {
                    basicMatrix[i][j] = matrix[i][j];
                } else {
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
        Fraction[][] newMatrix = new Fraction[rows - 1][cols + 1];

        if (rowToRemove >= 0)
            System.arraycopy(matrix, 0, newMatrix, 0, rowToRemove);

        if (rows - (rowToRemove + 1) >= 0)
            System.arraycopy(matrix, rowToRemove + 1, newMatrix, rowToRemove, rows - (rowToRemove + 1));

        this.matrix = newMatrix;
        this.rows--;
    }

    private List<List<Integer>> getCombinations(List<Integer> elements, int combinations) {
        List<List<Integer>> result = new ArrayList<>();
        generateCombinations(elements, combinations, 0, new ArrayList<>(), result);
        return result;
    }

    private void generateCombinations(List<Integer> elements, int comb_size, int start, List<Integer> current, List<List<Integer>> result) {
        if (current.size() == comb_size) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < elements.size(); i++) {
            current.add(elements.get(i));
            generateCombinations(elements, comb_size, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}
