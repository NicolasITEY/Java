package mvc;

import java.util.ArrayList;
import java.util.List;

/// Abstract class representing a controller in the MVC architecture
public abstract class Controller {
		/// Sub-controllers
		protected final List<Controller> subControllers = new ArrayList<>();
	
		/// Related [Model]
		protected final Model model;

		/// Related [View]
		protected final View view;

		/// Custom constructor
		///
		/// @param p_model Related [Model]. Should not be null.
		/// @param p_view Related [View]. Should not be null.
		protected Controller(Model p_model, View p_view) {
				this.model = p_model;
				this.view = p_view;
		}

		/// {@return the related [Model]}
		public Model getModel() {
				return this.model;
		}

		/// {@return the related [View]}
		public View getView() {
				return this.view;
		}
}
