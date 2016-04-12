public class GradebookOps
{
	public static int findStudent(String studentName, String[] allStudentNames, boolean alphabetical)
	{
		studentName=studentName.toLowerCase();
		for(int i=0;i<allStudentNames.length; i++){
			String temp = allStudentNames[i].toLowerCase();
			if (temp.equals(studentName))
				return i;
		}

		return -1;
	}

	public static double computeGrade(int studentIndex, int[][] scoreTable, int[] itemPointValues)
	{
		double total = 0;
		double grade = 0;
		for(int i =0; i <scoreTable[studentIndex].length; i++){
			if(scoreTable[studentIndex][i]==-1){
				grade=grade+1;
				total= total - itemPointValues[i];
			}
			grade = grade + scoreTable[studentIndex][i];
			total = itemPointValues[i]+total;
		}
		return (grade/total*100);

	}

	public static double[] computeAllGrades(int[][] scoreTable, int[] itemPointValues)
	{
		double [] avg = new double[scoreTable.length];
		for(int x = 0; x<scoreTable.length; x++)
				avg[x]=computeGrade(x, scoreTable, itemPointValues);
		return avg;
	}

	public static double computeClassAverage(int item, int[][] scoreTable)
	{
		int count= 0 ;
		double avg =0;
		for(int x = 0; x <scoreTable.length; x++){
			if(scoreTable[x][item]==-1){
				count--;
				avg=avg+1;
			}
			avg= avg+scoreTable[x][item];
			count++;
		}
		avg = avg/count;
		return avg;
		
	}



}