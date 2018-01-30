/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mathplugin;

import eu.mihosoft.vrl.annotation.ComponentInfo;
import eu.mihosoft.vrl.annotation.MethodInfo;
import eu.mihosoft.vrl.annotation.ParamInfo;
import eu.mihosoft.vrl.annotation.ParamGroupInfo;
import java.io.Serializable;

/**
 *
 * @author tim
 */
@ComponentInfo(name="Matrix", category="Math")
public class Matrix implements Serializable,LUSolverInterface{
    
  private static final long serialVersionUID=1;
  public int dim1;
  public int dim2;
  
  public double[][] data;
  
  public Matrix() {
      this.dim1 = 0;
      this.dim2 = 0;
      this.data = new double[0][0];
  }
  
  public Matrix(int dim1, int dim2) {
      this.dim1 = dim1;
      this.dim2 = dim2;
      data = new double[dim1][dim2];      
  }
    
  public Matrix(Matrix A) {
      dim1 = A.dim1;
      dim2 = A.dim2;
      data = new double[dim1][dim2];
      for(int i = 0; i < dim1; i++) {
          for(int j = 0; j < dim2; j++) {
              data[i][j] = A.data[i][j];
          }
      }
  }
  
  public Matrix(double[][] A) {
      dim1 = A.length;
      dim2 = A[0].length;
      data = new double[dim1][dim2];
      for(int i = 0; i < dim1; i++) {
          for(int j = 0; j < dim2; j++) {
              data[i][j] = A[i][j];
          }
      }
  }
  
    public Matrix(double[] vector, int stride) {
        if(data.length % stride != 0) {
            throw new RuntimeException("vector length not divisible by stride");
        }
        
        dim1 = data.length/stride;
        dim2 = stride;
        
        for(int i = 0; i < dim1; i++) {
          for(int j = 0; j < dim2; j++) {
              data[i][j] = vector[i*stride + j];
          }
      }
    }
  
  public static Matrix FromArray(double[][] v) {
        return new Matrix(v);
    }
  
  public static Matrix FromVectorStride(double[] v, int stride) {
      return new Matrix(v,stride);
  }
  
  public Vector MultiplyVector(Vector v) {
      
      if(v.length != this.dim2) {
        throw new RuntimeException("cant multiply with vector, wrong dimensions");
      }
      
      Vector result = new Vector(dim1);
      
      for(int i = 0; i < dim1; i++) {
          for(int j = 0; j < dim2; j++) {
            result.data[i] += data[i][j] * v.data[j];
          }
      }
      return result;
  }
  
  public Matrix MultiplyMatrix(Matrix A) {
      
    if(A.dim1 != this.dim2) {
        throw new RuntimeException("cant multiply matrices, wrong dimensions");
    }
      
    Matrix Result = new Matrix(dim1, A.dim2);

    for(int i = 0; i < dim1; i++){        
      for(int j = 0; j < A.dim2; j++){
        for(int n = 0; n < dim2; n++) {
          Result.data[i][j] += data[i][n] * A.data[n][j];
        }
      }        
    }

    return Result;
  }
  
  public void scale(double factor) {
      for(int i = 0; i < dim1; i++) {
          for(int j = 0; j < dim2; j++) {
              this.data[i][j] *= factor;
          }
      }
  }
  
  public void subtract(Matrix A) {
      if(A.dim1 != dim1 || A.dim2 != dim2) {
          throw new RuntimeException("Cant subtract matrices with different dimensions");
      }
      
      for(int i = 0; i < dim1; i++) {
          for(int j = 0; j< dim2; j++) {
              this.data[i][j] -= A.data[i][j];
          }
      }
  }
  
  public static Matrix Identity(int size) {
      Matrix result = new Matrix(size, size);
      for(int i = 0; i < size; i++) {
          result.data[i][i] = 1;
      }
      
      return result;
  }
  
  public static Matrix Matmul(Matrix A, Matrix B) {
      Matrix C = new Matrix(A);
      C.MultiplyMatrix(B);
      return C;
  }
  
    @MethodInfo(name="", valueName="Matrix LU")
  public static Matrix LUDecomposition(
    @ParamInfo(name="Matrix A")   Matrix Input
    ) {

    int N = Input.dim1;

    Matrix A = new Matrix(N,N);

    for(int i = 0; i < N; i++) {
      for(int j = 0; j < N; j++) {
        A.data[i][j] = Input.data[i][j];
      }
    }
    
    for(int k = 0; k < N-1; k++){
      if(A.data[k][k] == 0){
        throw new RuntimeException("Input matrix A was singular.");
      }
      for(int j = k+1; j < N; j++){
        A.data[j][k] = (A.data[j][k]) / (A.data[k][k]);
        for(int i = k+1; i < N; i++){
          A.data[j][i] = A.data[j][i] - A.data[k][i]*A.data[j][k];
        }
      }
    }

    return A;
  }

   @MethodInfo(name="", valueName="Vector x")
   public static Vector SolveAxb(
      @ParamInfo(name="Matrix LU")   Matrix LU,
      @ParamInfo(name="Vector b")   Vector b
      ) {
  
      int N = b.length;
      
      Vector x = new Vector(N);
      Vector y = new Vector(N);
      
      for(int i = 0; i < N; i++){
        y.data[i] = b.data[i];
        for(int k=0; k <= i-1; k++){
          y.data[i] -= LU.data[i][k]* y.data[k];
        }       
      }

      for(int i = N-1; i >= 0; i--){
        x.data[i] = y.data[i];
        for(int k=i+1; k < N; k++){
          x.data[i] -= LU.data[i][k]* x.data[k];
        }        

        x.data[i] = (x.data[i])/(LU.data[i][i]);
      }
  
      return x;
    }

   @MethodInfo(name="", valueName="Matrix A^(-1)")
   public static Matrix InvertMatrix(
      @ParamInfo(name="Matrix A")   Matrix A
      ) {
  
      int N = A.dim1;
      
      Matrix LU = LUDecomposition(A);

      Matrix Result = new Matrix(N,N);
      Vector b = new Vector(N);
      Vector x = new Vector(N);

      for(int i = 0; i < N; i++){
        
        for(int n = 0; n < N; n++){
          b.data[n] = (n == i) ? 1 : 0;
        }

        x = SolveAxb(LU, b);

        for(int n = 0; n < N; n++){
          Result.data[n][i] = x.data[n];
        }
      }

      return Result;
    }  
   
   private void setRow(int index, Vector row) {
       if(dim2 != row.length)
           throw new RuntimeException("Row length is not the same");
       
       if(dim1 <= index)
           throw new RuntimeException("Index i not in matrix");
       
       for(int i = 0; i < dim2; i++) {
           data[index][i] = row.data[i];
       }
   }
   
   public static double[][] getData(Matrix A) {
       return A.data;
   }
   
  @Override
   public String toString() {
       String result = "";
       for(int i = 0; i< dim1; i++) {
           for(int j = 0; j < dim2; j++) {
               result += data[i][j] + " ,";
           }
           result += "\n";
       }
       
       return result;
   }

    @Override
    public double[] solve(double[][] A, double[] b) {
        Matrix MatA = new Matrix(A);
        Vector Vecb = new Vector(b); 
        
        return Matrix.SolveAxb(MatA, Vecb).data;
    }
}
