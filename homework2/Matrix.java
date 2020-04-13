import java.text.DecimalFormat;
import java.util.Random;

public class Matrix {
    private double[][] matrix;
    private int row, column;

    public Matrix() {
        this(0,0);
    }

    public Matrix(int size){
        this(size,size);
    }

    public Matrix(int row, int column){
        this.row = row;
        this.column = column;
        matrix = new double[row][column];
    }

    public void generateMatrix(){
        Random random = new Random();
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                matrix[i][j]  = Math.round(random.nextDouble()*10000)/100.;
            }
        }
    }

    public Matrix sum(Matrix matrix2){
        Matrix result = new Matrix(row, column);
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                result.matrix[i][j] = matrix[i][j] + matrix2.matrix[i][j];
            }
        }
        return result;
    }

    public Matrix mul(Matrix matrix2){
        Matrix result = new Matrix(row, matrix2.column);
        for(int i = 0; i < row; i++){
            for(int j = 0; j < matrix2.column; j++) {
                result.matrix[i][j] = 0;
                for (int k = 0; k < column; k++) {
                    result.matrix[i][j] += matrix[i][k] * matrix2.matrix[k][j];
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                result += new DecimalFormat("#0.00").format(matrix[i][j]) + ' ';
            }
            result += "\n";
        }
        return result;
    }

    private Matrix matrixWithoutRowAndColumn(int size, int column){
        Matrix temp = new Matrix(size-1,size-1);
        int tempColumn = 0;
        for(int j = 0; j < size; j++){
            if(j == column){
                continue;
            }
            for(int i = 1; i < size; i++){
                temp.matrix[i-1][tempColumn] = matrix[i][j];
            }
            tempColumn++;
        }
        return temp;
    }

    public double determinant(){ //разложение по первой строке
        if(row == 1){
            return matrix[0][0];
        }
        else if(row == 2){
            return matrix[0][0]*matrix[1][1] - matrix[0][1]*matrix[1][0];
        }
        else{
            double result = 0.0;
            int temp = 1;
            for(int i = 0; i < column; i++){
                result += temp * matrix[0][i] * matrixWithoutRowAndColumn(row, i).determinant();
                temp *= -1;
            }
            return result;
        }
    }
}
