/*
   Copyright (c) 2010 Diego Pizzocaro
   Permission is hereby granted, free of charge, to any person
   obtaining a copy of this software and associated documentation
   files (the "Software"), to deal in the Software without
   restriction, including without limitation the rights to use,
   copy, modify, merge, publish, distribute, sublicense, and/or sell
   copies of the Software, and to permit persons to whom the
   Software is furnished to do so, subject to the following
   conditions:

   The above copyright notice and this permission notice shall be
   included in all copies or substantial portions of the Software.

   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
   OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
   HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
   WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
   FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
   OTHER DEALINGS IN THE SOFTWARE.
   

 * ------------------------------------------------------------
 * "THE BEERWARE LICENSE" (Revision 42):
 * Diego Pizzocaro wrote this code. As long as you retain this 
 * notice, you can do whatever you want with this stuff. If we
 * meet someday, and you think this stuff is worth it, you can
 * buy me a beer in return.
 * ------------------------------------------------------------
 */

package GlobalMessenger;

/**
 *
 * Message java object
 * (exchanged among Nodes through the GlobalMessenger)
 * 
 * @author Diego Pizzocaro
 * 
 */
public class Message {
	
	Node sender;
	Node receiver;
	Object content;
	double timestamp;
	
	public Message(Node sender, Node receiver, Object content, double timestamp) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = timestamp;
	}
	
	public Node getSender() {
		return sender;
	}
	public void setSender(Node sender) {
		this.sender = sender;
	}
	public Node getReceiver() {
		return receiver;
	}
	public void setReceiver(Node receiver) {
		this.receiver = receiver;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	public double getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(double timestamp) {
		this.timestamp = timestamp;
	}
	
	

}
