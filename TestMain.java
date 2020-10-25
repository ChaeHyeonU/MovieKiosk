package movie;

public class TestMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Movie m = new Movie();
		LogIn.inputOrder(m);
		
		SelectSeat s = new SelectSeat("1","10/19","12:30~14:30"); //상영관 , 날짜 , 시간
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


