package app;

public class Views {
	private static void commonOptions() {
		System.out.println("l   => Logout");
		System.out.println("exit=> Exit");
		System.out.println("------------------------");
	}

	private static void userOptions() {
		System.out.println("1   => Send Message");
		System.out.println("2   => Read received messages");
		System.out.println("3   => Read sent messages");

	}

	private static void editorOptions() {
		System.out.println("4   => Edit a Message");
		System.out.println("5   => Delete a Message");
	}

	private static void adminOptions() {
		System.out.println("6   => Register new user");
		System.out.println("7   => Remove user");
		System.out.println("8   => Update user");
	}

	public static void userMainMenu() {
		System.out.println("___---User Menu---___");
		userOptions();
		commonOptions();
	};

	public static void editorMainMenu() {
		System.out.println("___---Editor Menu---___");
		userOptions();
		editorOptions();
		commonOptions();
	}

	public static void adminMainMenu() {
		System.out.println("___---Admin Menu---___");
		userOptions();
		editorOptions();
		adminOptions();
		commonOptions();
	}

	public static void welcome() {
		System.out.println("___Message Application___");
		System.out.println("*************************");
		System.out.println("Welcome to the application");
		System.out.println("Please log in to continue\n");
	}

}
