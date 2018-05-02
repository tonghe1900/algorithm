/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
 // 1 ->2 ->3
 //  3->2->1

  class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
  }
public class ReverseList {
    private static ListNode newHead = null;
    public ListNode reverseList(ListNode head) {
    	ListNode end = reverse(head);
    	end.next  = null;
        return newHead;
    }
    
    private ListNode reverse(ListNode head){
        if(head == null) return null;
        if(head.next == null) {
            newHead = head;
            return head;
        }
        ListNode reverseNext = reverse(head.next);
        reverseNext.next = head;
        
        return head;
        
    }
    //  1 ->2 ->3
    
    public ListNode reverseListIterative(ListNode head) {
    	if(head == null || head.next == null) return head;
    	ListNode prev = null;
    	ListNode runner = head;
    	while(runner != null){
    		ListNode next = runner.next;
    		runner.next = prev;
    		prev = runner;
    		runner = next;
    	}
    	return prev;
        
    	
    }
    
    
    
    
    
    public static void main(String [] args){
    	ListNode first = new ListNode(1);
    	first.next  = new ListNode(2);
    	first.next.next = new ListNode(3);
    	ListNode copy = first;
    	while(first != null){
    		System.out.print(first.val+ ",");
    		first = first.next;
    	}
    	 //new ReverseList().reverseList(copy);
    	ListNode reverseHead =  new ReverseList().reverseListIterative(copy);
    	while(reverseHead != null){
    		System.out.print(reverseHead.val+ ",");
    		reverseHead = reverseHead.next;
    	}
    	 
    }
}
