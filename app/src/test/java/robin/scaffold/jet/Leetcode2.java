package robin.scaffold.jet;

import org.junit.Test;

public class Leetcode2 {
    /**
     * Definition for singly-linked list.
     *
     * */
     public class ListNode {
         int val;
         ListNode next;
         ListNode(int x) { val = x; }
      }

      @Test
      public void test() {
          ListNode d1 = new ListNode(2);
          d1.next = new ListNode(4);
          d1.next.next = new ListNode(3);

          ListNode d2 = new ListNode(5);
          d2.next = new ListNode(6);
          d2.next.next = new ListNode(4);
          ListNode r = addTwoNumbers(d1, d2);
          r.toString();
      }

    /**
     * (2 -> 4 -> 3) + (5 -> 6 -> 4)
     * 7 -> 0 -> 8
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode r1 = new ListNode(0);
        return getSumNode(l1, l2, 0, r1);
    }

    private ListNode getSumNode(ListNode l1, ListNode l2, int index, ListNode result) {
        boolean up = false;
        boolean upNext = false;
        ListNode targetNode = getNodeByIndex(index, result);
        int sum = getSum(l1, l2, targetNode);
        if(sum >= 10) {
            sum = sum % 10;
            up = true;
        } else {
            up = false;
        }
        targetNode.val = sum;
        if((l1 == null && l2 == null)
                || (l1 != null && l2 != null && l1.next == null && l2.next == null)
                || (l1 == null && l2 != null && l2.next == null)
                || (l2 == null && l1 != null && l1.next == null)) {
            if(up) {
                targetNode.next = new ListNode(1);
                index ++;
            }
            return result;
        } else {
            if(up) {
                targetNode.next = new ListNode(1);
            } else {
                targetNode.next = new ListNode(0);
            }
            index ++;
            return getSumNode(l1 != null ? l1.next : null, l2 != null ? l2.next : null, index, result);
        }
    }

    private ListNode getNodeByIndex(int index, ListNode node) {
        ListNode targetNode = node;
        for (int i = 0; i < index;i++) {
            if(targetNode.next != null) {
                targetNode = targetNode.next;
            }
        }
        return targetNode;
    }

    private int getSum(ListNode l1, ListNode l2, ListNode l3) {
        int v1,v2,v3;
        if(l1 == null) {
            v1 = 0;
        } else {
            v1 = l1.val;
        }
        if(l2 == null) {
            v2 = 0;
        } else {
            v2 = l2.val;
        }
        if(l3 == null) {
            v3 = 0;
        } else {
            v3 = l3.val;
        }
        return v1 + v2 + v3;
    }
}
