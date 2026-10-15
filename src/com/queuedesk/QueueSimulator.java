package com.queuedesk;
import java.util.Scanner;
public class QueueSimulator {
    public static void main (String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.print("Starting ticket number: ");
        int startTicket = sc.nextInt();

        System.out.print("Number of customers: ");
        int customerCount = sc.nextInt();

        if (customerCount <= 0){
            System.out.println("Nothing to simulate");
            sc.close();
            return;
        }

        int vipCount = 0;
        int recheckCount = 0;
        int servedCount = 0;

        for(int position =1; position <= customerCount; position++){
            int ticketNumber = startTicket+position-1;
            System.out.print("Now Serving Ticket #"+ ticketNumber);

            if (position%5 == 0){
                System.out.print (" [VIP Lane]");
                vipCount++;
            }
            if(position%10 == 0){
                System.out.print (" [Priority Recheck]");
                recheckCount++;
            }

            System.out.println();
            servedCount++;
        }
        System.out.println("Served: "+ servedCount);
        System.out.println("VIP Lane: "+ vipCount);
        System.out.println("Priority ReCheck: "+ recheckCount);

        sc.close();

    }
}
