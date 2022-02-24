// Mathew Jacobson - I pledge my honor that I have abided by the Stevens Honor System - Matthew Jacobson

public class Swapper implements Runnable {
    private int offset;
    private Interval interval;
    private String content;
    private char[] buffer;

    public Swapper(Interval interval, String content, char[] buffer, int offset) {
        this.offset = offset;
        this.interval = interval;
        this.content = content;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        // TODO: Write the specified content into the buffer. Helper methods may be used to retrieve the content and insert it into the proper spot in the buffer
    	// take content and write to buffer @ offset+interval
//    	char[] newcontent = content.toCharArray(); // make it easier to work with
    	// location in content to write to the buff
    	for(int trackbuff = offset,trackCont = interval.getX(); trackbuff < buffer.length && trackCont <= interval.getY(); trackbuff++, trackCont++) { 
    			buffer[trackbuff] = content.charAt(trackCont); // write data
    	}
    }
}

//for(int x = offset; x < content.length(); x++) { //start in the right place (offset and start writing to the buffer) 
//	//now write
//	for(int y = 0; y < newcontent.length; y++) {
//		while(x<buffer.length) {
//		buffer[x] = newcontent[y]; // start placing chars in the buffer until we run out of space or 
//		}
//	}
//}

//for(int x = offset, y = interval.getX() ; x < buffer.length && y <= interval.getY(); x++,y++ ) { // location in content to write to the buff

//for(int trackCont = interval.getX(); trackCont <= interval.getY();trackCont++) {
//	buffer[trackbuff] = newcontent[trackCont];
//}