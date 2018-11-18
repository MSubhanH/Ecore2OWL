/**
 */
package family;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Family</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link family.Family#getMember <em>Member</em>}</li>
 * </ul>
 *
 * @see family.FamilyPackage#getFamily()
 * @model
 * @generated
 */
public interface Family extends EObject {
	/**
	 * Returns the value of the '<em><b>Member</b></em>' containment reference list.
	 * The list contents are of type {@link family.Person}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Member</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Member</em>' containment reference list.
	 * @see family.FamilyPackage#getFamily_Member()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<Person> getMember();

} // Family
