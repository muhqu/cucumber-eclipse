package cucumber.eclipse.editor.editors;

import gherkin.lexer.LexingError;
import gherkin.parser.ParseError;
import gherkin.parser.Parser;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

import cucumber.eclipse.editor.Activator;
import cucumber.eclipse.editor.preferences.ICucumberPreferenceConstants;

public class GherkinFormatterUtil {

	public static String format(String contents) {
		// set up
		StringWriter output = new StringWriter();
		PrintWriter out = new PrintWriter(output);
		
		PrettyFormatter formatter = new PrettyFormatter(out, true, false);

		// configurable preferences
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		formatter.setRightAlignNumericValues(store.getBoolean(ICucumberPreferenceConstants.PREF_FORMAT_RIGHT_ALIGN_NUMERIC_VALUES_IN_TABLES));
		formatter.setCenterSteps(store.getBoolean(ICucumberPreferenceConstants.PREF_FORMAT_CENTER_STEPS));
		formatter.setPreserveBlankLineBetweenSteps(store.getBoolean(ICucumberPreferenceConstants.PREF_FORMAT_PRESERVE_BLANK_LINE_BETWEEN_STEPS));
		
		// parse 
		new Parser(formatter).parse(contents, "", 0);

		out.flush();
		return output.toString();
	}

	public static TextEdit formatTextEdit(String contents, int i, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void format(IEditorPart editorPart) {
		ITextEditor editor = (ITextEditor) editorPart;
		Shell shell = editorPart.getSite().getShell();
		IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		String contents = doc.get();
		try {
			String formatted = GherkinFormatterUtil.format(contents);
			doc.replace(0, doc.getLength(), formatted);
		} catch (ParseError e) {
			MessageDialog.openInformation(shell, "Unable to pretty format.",
					"One can only format a feature file that has no parse errors: \n"
							+ "The following parse error was encountered: ["
							+ e.getMessage() + "]");

		} catch (LexingError e) {
			MessageDialog.openInformation(shell, "Unable to pretty format.",
					"One can only format a feature file that has no lexing errors: \n"
							+ "The following lex error was encountered: ["
							+ e.getMessage() + "]");
		}
		
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}

}
