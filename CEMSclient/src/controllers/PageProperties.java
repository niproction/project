package controllers;

public class PageProperties {
	public enum Animation {
		FADE_IN, FADE_OUT, SLIDE_LEFT, SLIDE_RIGHT, SLIDE_UP, SLIDE_DOWN
	}

	public enum Page {
		LOGIN {
			@Override
			String GET_Title() {
				return "Login page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/login.fxml";
			}

			@Override
			Animation GET_ON_Scene_Load_Animation() {
				return Animation.FADE_IN;
			}

			@Override
			Animation GET_ON_Scene_Unload_Animation() {
				return Animation.FADE_OUT;
			}
		},
		HOME {
			@Override
			String GET_Title() {
				return "HOME page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/Main.fxml";
			}

			@Override
			Animation GET_ON_Scene_Load_Animation() {
				return Animation.FADE_IN;
			}

			@Override
			Animation GET_ON_Scene_Unload_Animation() {
				return Animation.FADE_OUT;
			}
		},
		ADD_NEW_QUESTION {
			@Override
			String GET_Title() {
				return "AddQuestion page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/AddnewQuestion.fxml";
			}

			@Override
			Animation GET_ON_Scene_Load_Animation() {
				return Animation.FADE_IN;
			}

			@Override
			Animation GET_ON_Scene_Unload_Animation() {
				return Animation.FADE_OUT;
			}
		};

		abstract String GET_Title();

		abstract String GET_FxmlFile();

		abstract Animation GET_ON_Scene_Load_Animation();

		abstract Animation GET_ON_Scene_Unload_Animation();

	}
}