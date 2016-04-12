import java.util.*;
import java.awt.Color;

public class CellSim{

	public static void main(String[] args){
		System.out.println("please enter size of grid.");
		int n = IO.readInt();
		if(n<0){
			IO.reportBadInput();
			return;
		}
		char[][] tissue = new char[n][n];
		System.out.println("Please enter threshold value in percent form (between 0 -100)");
		int threshold = IO.readInt();
		System.out.println("Please enter number of max rounds.");
		int maxRounds=IO.readInt();
		System.out.println("Please enter number of times the board should be printed int x rounds. (Fresquencey of printing. i splled frequency wrong.");
		int frequency = IO.readInt();
		System.out.println("Please enter percent blank");
		int percentBlank = IO.readInt();
		System.out.println("Pease enter percentX");
		int percentX = IO.readInt();
		if(percentBlank < 0 || percentBlank > 100 || percentX<0 || percentX>100 || threshold < 0 || threshold > 100 || n < 0 || maxRounds <= 0 || frequency < 0){
			IO.reportBadInput();
			return;
		}
		assignCellTypes(tissue, percentBlank, percentX);
		CellSimGUI hi = new CellSimGUI(n, 1);
		System.out.println("Initial board is: ");
		printTissue(tissue, hi, n, threshold);
		System.out.println("");
		int x = 0;
		int i =0;
		int a =0;
		int max = 1;
		int rounds = 0;
		double percentUnsatisfied = 0 ;
		int counter = 0;
		double satisfied=0;
		while(boardSatisfied(tissue, threshold) == false){
			counter = moveAllUnsatisfied(tissue, threshold)+counter ;
			x++;
			if(x%frequency == 0){
				printTissue(tissue, hi, n, threshold);
				System.out.println("");
				if(boardSatisfied(tissue,threshold) == false){
					System.out.println("Board is not satisfied");
				}
				else
					System.out.println("Board is satisfied.");
			}
			if(max == maxRounds){
				System.out.println("Board cannot be satisfied within max rounds givent.");
				for(i = 0; i <tissue.length; i++){
					for(a = 0; a<tissue.length; a++){
						if(isSatisfied(tissue, i, a, threshold) == false)
							percentUnsatisfied++;
						else
							satisfied++;
					}
				}
				percentUnsatisfied = (satisfied / (percentUnsatisfied+satisfied));
				System.out.println("This is the percent of satisfied cells : " + (percentUnsatisfied*100) +"%");
				System.out.println("THis is the number of movements that occured: " +counter);
				return;
			}
			max++;
			rounds ++; 
		}
		System.out.println("This took " + rounds + " number of rounds");
		System.out.println("THis is the number of movements that occured: " +counter);
		System.out.println("Baord is satisfied. Final board is:");
		printTissue(tissue, hi, n, threshold);
	}

	public static void printTissue(char[][] tissue, CellSimGUI hi, int n, int thresh){
		
		
		for(int i = 0; i<tissue.length; i++){
			for(int x = 0;x<tissue[i].length;x++){
				if(tissue[i][x] == ' ')
					hi.setCell(i,x, Color.DARK_GRAY);
				if(tissue[i][x] == 'X')
					hi.setCell(i,x, Color.cyan);
				if(tissue[i][x] == 'O')
					hi.setCell(i,x, Color.pink);
				
			}
		}
	}

	public static void assignCellTypes(char[][] tissue, int percentBlank, int percentX){
		if(percentBlank >100 || percentX>100) // Error Check
			System.out.println("Error");
		int percentLeft = 100-percentBlank;
		int percentO = 100-percentX;
		int total = tissue[0].length*tissue.length;
		int count = 1;
		int newcount=1;
		double x = ((double)(percentX))/100*percentLeft/100*total;
		if(x%1!=0)
			x=(int)(x)+1;
		double o =(double)(percentO)/100*percentLeft/100*total;
		o=(int)(o);
		for(int i = 0; i<tissue.length; i++){
			for(int y = 0;y<tissue.length;y++){
				if(count<=x){ //sets x cells
					tissue[i][y]='X';
					count++;
				}
				else if(newcount<=o){ //sets o cells.
					tissue[i][y]='O';
					newcount++;
				}
				else
					tissue[i][y]=' ';

			}

		}
		int NumberX = 0;
		int NumberO= 0;
		int NumberBlank=0;
		for(int i = 0; i<tissue.length; i++){  //counts number of cells
			for(int y = 0;y<tissue.length;y++){
				if(tissue[i][y]=='X')
					NumberX++;
				if(tissue[i][y]=='O')
					NumberO++;
				if(tissue[i][y]==' ')
					NumberBlank++;
			}

		}

		double random = 0;
		boolean counter = false;
		for(int i = 0; i<tissue.length; i++){  //randomizes placements.
			for(int y = 0;y<tissue.length;y++){
				do{
					random = (int)(Math.random()*3); // randomizes a number to be either 0,1, or 2.
					if(random==0 && NumberX>0){ //sets the cell to x if the number random number is 0.
						tissue[i][y] = 'X';
						NumberX--;
						counter = true;
					}
					else if(random==1 && NumberO>0){ //sets the cell to o if the random number is 1
						tissue[i][y] = 'O';
						NumberO--;
						counter = true;
					}
					else if(random==2 && NumberBlank>0){ // sets the cell to blank if the random number is 2.
						tissue[i][y] = ' ';
						NumberBlank--;
						counter = true;
					}
					else
						counter = false;
				}while(counter == false);
			}
		}
	}
	/**
	 * Given a tissue sample, and a (row,col) index into the array, determines if the agent at that location is satisfied.
	 * Note: Blank cells are always satisfied (as there is no agent)
	 *
	 * @param tissue a 2-D character array that has been initialized
	 * @param row the row index of the agent
	 * @param col the col index of the agent
	 * @param threshold the percentage of like agents that must surround the agent to be satisfied
	 * @return boolean indicating if given agent is satisfied
	 *
	 **/
	public static boolean isSatisfied(char[][] tissue, int row, int col, int threshold){
		int CellLength = tissue.length;
		double counter = 0;
		double counter2 = 0;

		if(tissue[row][col] == ' ')
			return true;

		if(row - 1 < 0 && col - 1 < 0)
		{
			for(int i = 0; i <= 1; i++)
			{
				for(int j = 0; j <= 1; j++)
				{
					if(i != row || j != col)
					{
						if(tissue[row][col] == tissue[i][j])
							counter++;	

						if(tissue[i][j] != ' ')
							counter2++;
					}
				}

			}
		}
		else if(row - 1 < 0 && col + 1 >= CellLength)
		{
			for(int i = 0; i <= 1; i++)
			{
				for(int j = CellLength - 2; j <= CellLength - 1; j++)
				{
					if(i != row || j != col)
					{
						if(tissue[row][col] == tissue[i][j])
							counter++;	

						if(tissue[i][j] != ' ')
							counter2++;
					}
				}

			}
		}
		else if(row + 1 >= CellLength && col - 1 < 0)
		{
			for(int i = CellLength - 2; i <= CellLength - 1; i++)
			{
				for(int j = 0; j <= 1; j++)
				{
					if(i != row || j != col)
					{
						if(tissue[row][col] == tissue[i][j])
							counter++;	

						if(tissue[i][j] != ' ')
							counter2++;
					}
				}

			}
		}
		else if(row + 1 >= CellLength && col + 1 >= CellLength)
		{
			for(int i = CellLength - 2; i <= CellLength - 1; i++)
			{
				for(int j = CellLength - 2; j <= CellLength - 1; j++)
				{
					if(i != row || j != col)
					{
						if(tissue[row][col] == tissue[i][j])
							counter++;	

						if(tissue[i][j] != ' ')
							counter2++;
					}
				}

			}
		}

		else if(row + 1 >= CellLength && (col - 1 >= 0 && col + 1 < CellLength))
		{
			for(int i = row - 1; i <= row; i++)
			{
				for(int j = col - 1; j <= col + 1; j++)
				{
					if(i != row || j != col)
					{
						if(tissue[row][col] == tissue[i][j])
							counter++;	

						if(tissue[i][j] != ' ')
							counter2++;
					}
				}

			}
		}

		else if(row - 1 < 0 &&(col - 1 >= 0 && col + 1 < CellLength))
		{
			for(int i = row; i <= row + 1; i++)
			{
				for(int j = col - 1; j <= col +1; j++)
				{
					if(i != row || j != col)
					{
						if(tissue[row][col] == tissue[i][j])
							counter++;	

						if(tissue[i][j] != ' ')
							counter2++;
					}
				}

			}
		}

		else if(col + 1 >= CellLength &&(row - 1 >= 0 && row + 1 < CellLength))
		{
			for(int i = row - 1; i <= row + 1; i++)
			{
				for(int j = col - 1; j <= col; j++)
				{
					if(i != row || j != col)
					{
						if(tissue[row][col] == tissue[i][j])
							counter++;	

						if(tissue[i][j] != ' ')
							counter2++;
					}
				}

			}
		}

		else if(col - 1 < 0 &&(row - 1 >= 0 && row + 1 < CellLength))
		{
			for(int i = row - 1; i <= row + 1; i++)
			{
				for(int j = col; j <= col + 1; j++)
				{
					if(i != row || j != col)
					{
						if(tissue[row][col] == tissue[i][j])
							counter++;	

						if(tissue[i][j] != ' ')
							counter2++;
					}
				}

			}
		}
		else
		{
			for(int i = row - 1; i <= row + 1; i++)
			{
				for(int j = col - 1; j <= col + 1; j++)
				{
					if(i != row || j != col)
					{
						if(tissue[row][col] == tissue[i][j])
							counter++;	

						if(tissue[i][j] != ' ')
							counter2++;
					}
				}

			}
		}
		if(counter2 == 0)
			return false;

		else if((counter/counter2)*100 >= (double)threshold)
			return true;

		else
			return false;


	}



	public static boolean boardSatisfied(char[][] tissue, int threshold){
		if(threshold == 0)
			return true;
		for(int i = 0; i <tissue.length; i++)
			for(int x = 0; x<tissue[0].length; x++){
				if(isSatisfied(tissue, i, x, threshold) == false)
					return false;
			}

		return true;
	}

	/**
	 * Given a tissue sample, move all unsatisfied agents to a vacant cell
	 *
	 * @param tissue a 2-D character array that has been initialized
	 * @param threshold the percentage of like agents that must surround the agent to be satisfied
	 *
	 **/
	public static int moveAllUnsatisfied(char[][] tissue, int threshold){
		ArrayList<Integer>  list2= new ArrayList<Integer>();
		int counter  =0 ;
		ArrayList<Integer>  list1= new ArrayList<Integer>();
		for(int i =0; i <tissue.length; i++){
			for(int x = 0; x<tissue[0].length;x++){
				if(isSatisfied(tissue,i,x,threshold) == false){
					list1.add(i);
					list2.add(x);
				}
			}
		}
		int num = list1.size();
		char temp;
		int count= 0;
		do{
			int rand = (int)(Math.random()*tissue.length);
			int random = (int)(Math.random()*tissue.length);
			if(tissue[rand][random] == ' '){
				temp = tissue[rand][random];
				tissue[rand][random] = tissue[list1.get(counter)][list2.get(counter)];
				tissue[list1.get(counter)][list2.get(counter)] = temp;
				counter++;
				count++;
			}
		}while(counter != num);
		return count;
	}
}