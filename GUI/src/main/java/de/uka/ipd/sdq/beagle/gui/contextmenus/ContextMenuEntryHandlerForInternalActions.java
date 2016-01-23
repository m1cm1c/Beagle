package de.uka.ipd.sdq.beagle.gui.contextmenus;

import de.uka.ipd.sdq.beagle.gui.GuiController;

import de.uka.ipd.sdq.pcm.gmf.seff.edit.parts.InternalAction2EditPart;
import de.uka.ipd.sdq.pcm.gmf.seff.edit.parts.InternalActionEditPart;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Handles the context menu entries, that start an analysis of an internal action.
 *
 * @author Roman Langrehr
 * @author Christoph Michelbach
 */
public class ContextMenuEntryHandlerForInternalActions extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil.getActiveMenuSelection(event);
		final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

		// prepare the list of internal actions
		final List<String> internalActions = new LinkedList<String>();
		for (final Object clickObject : structuredSelection.toList()) {
			String displayString = null;
			if (clickObject instanceof InternalActionEditPart) {
				final InternalActionEditPart internalActionEditPart = (InternalActionEditPart) clickObject;
				displayString =
					internalActionEditPart.getPrimaryShape().getFigureInternalActionFigureNameLabel().toString();
			} else {
				assert clickObject instanceof InternalAction2EditPart;
				final InternalAction2EditPart internalAction2EditPart = (InternalAction2EditPart) clickObject;
				displayString =
					internalAction2EditPart.getPrimaryShape().getFigureInternalActionFigureNameLabel().toString();
			}
			final String internalAction = displayString;
			internalActions.add(internalAction);
		}

		// create a new GUI and open it
		final GuiController guiController = new GuiController(internalActions);
		guiController.open();
		return null;
	}
}
