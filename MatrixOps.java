public class MatrixOps
{
	public static double[][] multiply(double[][] matrix1, double[][] matrix2)
	{
		
		int mat1 = matrix1.length;
		int rowcol = matrix1[0].length;
		int mat2 = matrix2[0].length;
		int rowcol2 = matrix2.length;
		if(rowcol!=rowcol2){
			return null;
		}
		double[][] mat = new double[mat1][mat2];
		for(int x = 0; x<mat1; x++){
			for(int y = 0; y<mat2;y++){
				for(int z = 0; z<rowcol;z++)
					mat[x][y]=mat[x][y]+(matrix1[x][z]*matrix2[z][y]);
				
				
			}
		}
		return mat;
	}
}