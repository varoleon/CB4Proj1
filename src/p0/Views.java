package p0;

public class Views {
	private static void userOptions() {
		System.out.println("s    => Send Message");
		System.out.println("i    => Inbox");
		System.out.println("l    => Logout");
		System.out.println("exit => Exit");
		System.out.println("------------------------");
	}

	private static void editorOptions() {
		System.out.println("o    => Read anyone's sent messages");
		System.out.println("e    => Edit a Message");
		System.out.println("del  => Delete a Message");
	}

	private static void adminOptions() {
		System.out.println("r    => Register new user");
		System.out.println("rm   => Remove user");
		System.out.println("u    => Update user");
	}

	public static void userMainMenu() {
		System.out.println("___---User Menu---___");
		userOptions();
	};

	public static void editorMainMenu() {
		System.out.println("___---Editor Menu---___");
		editorOptions();
		userOptions();
	}

	public static void adminMainMenu() {
		System.out.println("___---Admin Menu---___");
		adminOptions();
		editorOptions();
		userOptions();
	}

	public static void welcome() {
		System.out.println("___Message Application___");
		System.out.println("*************************");
		System.out.println("Welcome to the application");
		System.out.println("Please log in to continue");
		System.out.println("");
	}
}
