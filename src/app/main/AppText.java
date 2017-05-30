package app.main;

public enum AppText {
	APPLICATION_TITLE("Rejestrator ludzkich krok�w"),
	ACC_CHART("acceleration"),
	EULER_CHART("euler_orientation"),
	ACC_CHART_TITLE("Przyspieszenie"),
	EULER_CHART_TITLE("K�ty Eulera"),
	ACC_X("Przyspieszenie X"),
	ACC_Y("Przyspieszenie Y"),
	ACC_Z("Przyspieszenie Z"),
	EULER_X("X"),
	EULER_Y("Y"),
	EULER_Z("Z"),
	X_AXIX_CHART_LABEL("Czas(s)"),
	Y_AXIX_ACC_CHART_LABEL("Przyspieszenie(m/s2)"),
	Y_AXIX_EULER_CHART_LABEL("Stopnie"),
	PACKET_COUNTER("PacketCounter"),
	ACC_X_AXIS_IN_FILE("Acc_X"),
	ACC_Y_AXIS_IN_FILE("Acc_Y"),
	ACC_Z_AXIS_IN_FILE("Acc_Z"),
	EULER_X_AXIS_IN_FILE("Roll"),
	EULER_Y_AXIS_IN_FILE("Pitch"),
	EULER_Z_AXIS_IN_FILE("Yaw"),
	INFO_TEXT("INSTRUKCJA DOTYCZ�CA POPRAWNEJ ZAWARTO�CI PLIK�W\n" + 
				"1. Aby algorytm zadzia�a� poprawnie, dane musz� by� pobrane z czujnika ze stopy.\n" + 
				"2. Plik z danymi przyspieszenia:\n" + 
				"   - plik musi mie� rozszerzenie .txt;\n" + 
				"   - pierwszy wiersz zawiera nazwy danych (kolumn), rozdzielone �rednikami - PacketCounter;Acc_X;Acc_Y;Acc_Z:\n" + 
				"      * PacketCounter - jest to numer (identyfikator) pomiaru,\n" + 
				"      * Acc_X - pomiar przyspieszenia w osi X czujnika,\n" + 
				"      * Acc_Y - pomiar przyspieszenia w osi Y czujnika,\n" + 
				"      * Acc_Z - pomiar przyspieszenia w osi Z czujnika;\n" + 
				"   - ka�dy nast�pny wiersz zawiera dane dla ka�dej kolumny, dane musz� by� rozdzielone �rednikami;\n" + 
				"   - przyk�adowa zawarto�� pliku:\n" + 
				"      -----------------------------------------------------\n" + 
				"      PacketCounter;Acc_X;Acc_Y;Acc_Z\n" + 
				"      31476;2.490714;6.047431;7.286993\n" + 
				"      31477;2.495497;6.051737;7.319324\n" + 
				"      . . .\n" + 
				"      -----------------------------------------------------\n" + 
				"3. Plik z danymi k�t�w Eulera:\n" + 
				"   - plik musi mie� rozszerzenie .txt;\n" + 
				"   - pierwszy wiersz zawiera nazwy danych (kolumn), rozdzielone �rednikami - PacketCounter;Roll;Pitch;Yaw:\n" + 
				"      * PacketCounter - jest to numer (identyfikator) pomiaru,\n" + 
				"      * Roll - pomiar k�tu obrotu dooko�a osi X czujnika,\n" + 
				"      * Pitch - pomiar k�tu obrotu dooko�a osi Y czujnika,\n" + 
				"      * Yaw - pomiar k�tu obrotu dooko�a osi Z czujnika;\n" + 
				"   - ka�dy nast�pny wiersz zawiera dane dla ka�dej kolumny, dane musz� by� rozdzielone �rednikami;\n" + 
				"   - przyk�adowa zawarto�� pliku:\n" + 
				"      -----------------------------------------------------\n" + 
				"      PacketCounter;Roll;Pitch;Yaw\n" + 
				"      31476;39.688600;-14.734810;89.961635\n" + 
				"      31477;39.691981;-14.743071;89.959014\n" + 
				"      . . .\n" + 
				"      -----------------------------------------------------\n" + 
				"4. Nagranie:\n" + 
				"   - plik musi mie� rozszerzenie .mp4.\n");
	
	AppText(String get) {
		this.get = get;
	}
	private String get;

	public String get() {
		return get;
	}
}