import java.io.*;
import java.util.*;
// Mathew Jacobson - I pledge my honor that I have abided by the Stevens Honor System - Matthew Jacobson
public class TextSwap {

    private static String readFile(String filename, int chunkSize) throws Exception {
        String line;
        StringBuilder buffer = new StringBuilder();
        File file = new File(filename);
	// The "-1" below is because of this:
	// https://stackoverflow.com/questions/729692/why-should-text-files-end-with-a-newline
	if ((file.length()-1) % chunkSize!=0)
	    { throw new Exception("File size not multiple of chunk size"); };
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null){
            buffer.append(line);
        }
        br.close();
        return buffer.toString();
    }

    private static Interval[] getIntervals(int numChunks, int chunkSize) {
    	Interval[] done = new Interval[numChunks];
        int chunk = 0, track = chunkSize-1, start = 0, end = 0; 
        for(int x = 0; x < numChunks; x++) {
        	end=start+track;
        	Interval curr = new Interval(start,end);
        	start=end+1;
        	done[chunk] = curr; 
        	chunk++;
        }
        return done; 
    }

    private static List<Character> getLabels(int numChunks) {
        Scanner scanner = new Scanner(System.in);
        List<Character> labels = new ArrayList<Character>();
        int endChar = numChunks == 0 ? 'a' : 'a' + numChunks - 1;
        System.out.printf("Input %d character(s) (\'%c\' - \'%c\') for the pattern.\n", numChunks, 'a', endChar);
        for (int i = 0; i < numChunks; i++) {
            labels.add(scanner.next().charAt(0));
        }
        scanner.close();
        // System.out.println(labels);
        return labels;
    }

    private static char[] runSwapper(String content, int chunkSize, int numChunks) {
        List<Character> labels = getLabels(numChunks); // this holds pattern 
        Interval[] intervals = getIntervals(numChunks, chunkSize);
        // TODO: Order the intervals properly, then run the Swapper instances.
        //method output's a char[] so make that now 
        char[] done = new char[chunkSize*numChunks]; //where i am going to put the finished answer
        Arrays.fill(done,'-'); // for debugging purposes 
        int offset = 0; //keep track of location in buff so you dont have 2 threads writing to the same spot 
        //Order the intervals to the correct pattern ( am going to do this in the loop to search for the next letter in the pattern)
        int numberLoc = 0; //need this variable to store where the current letter is in the pattern to place in queue 
        System.out.println(labels);
        System.out.println(content);
        for(int x = 0; x<intervals.length;x++)
    		System.out.println("element "+x+" :"+intervals[x].toString());
        for(char currlabel : labels) { // once for each label
//        	System.out.println(currlabel); forgot 'a' is 97 not 1 
        	System.out.println(currlabel);
        	numberLoc = (int)currlabel-97; //correspond 'a' - 'e' (or whatever label) to the intervals
        	System.out.println(numberLoc);
        	Thread threadcurr = new Thread(new Swapper(intervals[numberLoc],content,done,offset)); // from javadocs, run the thread to write that interval to the buffer
        	threadcurr.start(); //from javadocs, this starts the created thread
        	System.out.println(done);
        	offset+=chunkSize; //update offset so that it doesnt conflict during write or overwrite 
//        	this whole part is completely wrong smh, I spent hours on this part  
//        	for(int x = 0; x < content.length();x++) { // find first occurrence for that letter a quick index of W
//        		System.out.println("content: "+content.charAt(x)+" Current Label:"+currlabel);
//        		if(currlabel == content.toLowerCase().charAt(x)) {
//        			System.out.println("GOT HERE");
//        			numberLoc = x;
//        			System.out.println(numberLoc);
//        			content=content.substring(chunkSize,content.length()-1);
//        			break;
//        		}
//        		break;
//        	}
//        	//now locate the corresponding interval and make thread
//        	for(int y = 0; y < intervals.length;y++) {
//        		if(intervals[y].getX() == numberLoc) {
//        			System.out.println(intervals[y]);
//        			Thread threadcurr = new Thread(new Swapper(intervals[y],content,done,offset)); //from java documentation, I also need to increment offset so that it goes to the next part
//        			threadcurr.start(); //start thread
//        			offset+=chunkSize; //update offset to keep moving along the "done"
//        			System.out.println(done);
//        		}
//        	}
        }
        return done; //return the updated buffer to be written back to the file
    }

    private static void writeToFile(String contents, int chunkSize, int numChunks) throws Exception {
        char[] buff = runSwapper(contents, chunkSize, contents.length() / chunkSize);
        PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
        writer.print(buff);
        writer.close();
    }

     public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TextSwap <chunk size> <filename>");
            return;
        }
        String contents = "";
        int chunkSize = Integer.parseInt(args[0]);
        try {
            contents = readFile(args[1],chunkSize);
//            System.out.println("finished read file");
            writeToFile(contents, chunkSize, contents.length() / chunkSize);
//            System.out.println("finished writing to file");
        } catch (Exception e) {
            System.out.println("Error with IO.");
            e.printStackTrace();
            return;
        }
    }
}

//
////make one thread for each chunk and call in new order 
//for(int x = 0; x <= numChunks; x++) {
//	//make thread
//	Thread run = new Thread(new Swapper(intervals[x],content,done,offset)); 
//	run.start();
//}
//return done;
