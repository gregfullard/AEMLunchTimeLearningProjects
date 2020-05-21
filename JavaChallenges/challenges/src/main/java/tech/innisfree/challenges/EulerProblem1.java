package tech.innisfree.challenges;

public class EulerProblem1 {

  public static void main(String[] args) {
    System.out.println("Project Euler - Problem 1");
    
    int input = 1000;
    int sum = 0;
    
    for (int i = 1; i<input; i++) {
      System.out.print("i = "+i+"  :");
      if ((i%3==0)||(i%5==0)) {
        System.out.println("Is a multiple of 3 or 5. Adding");
        sum += i;
      } else {
        System.out.println("Is NOT a multiple of 3 or 5. Ignoring");
      }
    }
    
    System.out.println(sum);
  }
  
}
