package domain;

public class MockNetIODelay {
	

	
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("I am sending message by network");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("I've got message in return");
	}
}
