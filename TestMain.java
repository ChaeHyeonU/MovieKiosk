package movie;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Movie m = new Movie();
		LogIn.inputOrder(m);		
		SelectSeat s = new SelectSeat(m.getInform2()[0],m.getInform2()[1],m.getInform2()[2]); //�󿵰� , ��¥ , �ð�
		s.GetSeat();
		s.showSeat();
		s.SelectPerson();
		s.SelSeat();
		s.SeatDivision();
		String[][] str = s.getSelectedSeat();
		PriceManager price = new PriceManager(LogIn.nowID, m.getInform(), str);
		price.priceCalculator();
		
		MovieManager.managerInput();
		
			
	}
}


