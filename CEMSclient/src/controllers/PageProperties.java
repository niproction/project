package controllers;

public class PageProperties {
	public enum Animation {
		FADE_IN, FADE_OUT, SLIDE_LEFT, SLIDE_RIGHT, SLIDE_UP, SLIDE_DOWN
	}

	public enum Page {
		
		// General screens
		/////////////////////////////////////////////////////////////////////////////////
		
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
		
		Main {
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
		
		TEMP {
			@Override
			String GET_Title() {
				return "Login page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/temp.fxml";
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
		STATISTICAL_REPORTS_BY {
			@Override
			String GET_Title() {
				return "AddQuestion page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/statisticalReportBy.fxml";
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
		
		//Student pages comes here
		///////////////////////////////////////////////////////////////////////////////////////////
		
		HomePage_Student {
			@Override
			String GET_Title() {
				return "Student - home page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/StudentHomePage.fxml";
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
		
		
		
		
		
		
		//Teacher pages comes here
		////////////////////////////////////////////////////////////////////////////////////////
		
		HomePage_Teacher {
			@Override
			String GET_Title() {
				return "Teacher - home page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/TeacherHomePage.fxml";
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
			
		},
		EDIT_QUESTION {
			@Override
			String GET_Title() {
				return "Edit question page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/editQuestion.fxml";
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
		CREATE_EXAM {
			@Override
			String GET_Title() {
				return "Login page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/CreateExam.fxml";
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
			
			
		
		
		//Principal pages comes here
		///////////////////////////////////////////////////////////////////////////////////////////////
		
		HomePage_Principal {
			@Override
			String GET_Title() {
				return "Principal - home page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/PrincipalHomePage.fxml";
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
		
		STATISTICAL_REPORTS {
			@Override
			String GET_Title() {
				return "AddQuestion page";
			}

			@Override
			String GET_FxmlFile() {
				return "../gui/statisticalReport.fxml";
			}

			@Override
			Animation GET_ON_Scene_Load_Animation() {
				return Animation.FADE_IN;
			}

			@Override
			Animation GET_ON_Scene_Unload_Animation() {
				return Animation.FADE_OUT;
			}
			
		}
	
			
		;

		
		
		
		abstract String GET_Title();

		abstract String GET_FxmlFile();

		abstract Animation GET_ON_Scene_Load_Animation();

		abstract Animation GET_ON_Scene_Unload_Animation();

	}
}