/**
 */
package family;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Person</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link family.Person#getName <em>Name</em>}</li>
 *   <li>{@link family.Person#getHasFather <em>Has Father</em>}</li>
 *   <li>{@link family.Person#getHasBrother <em>Has Brother</em>}</li>
 *   <li>{@link family.Person#getHasUncle <em>Has Uncle</em>}</li>
 * </ul>
 *
 * @see family.FamilyPackage#getPerson()
 * @model
 * @generated
 */
public interface Person extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see family.FamilyPackage#getPerson_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link family.Person#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Has Father</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Has Father</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Has Father</em>' reference.
	 * @see #setHasFather(Person)
	 * @see family.FamilyPackage#getPerson_HasFather()
	 * @model required="true"
	 * @generated
	 */
	Person getHasFather();

	/**
	 * Sets the value of the '{@link family.Person#getHasFather <em>Has Father</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Has Father</em>' reference.
	 * @see #getHasFather()
	 * @generated
	 */
	void setHasFather(Person value);

	/**
	 * Returns the value of the '<em><b>Has Brother</b></em>' reference list.
	 * The list contents are of type {@link family.Person}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Has Brother</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Has Brother</em>' reference list.
	 * @see family.FamilyPackage#getPerson_HasBrother()
	 * @model
	 * @generated
	 */
	EList<Person> getHasBrother();

	/**
	 * Returns the value of the '<em><b>Has Uncle</b></em>' reference list.
	 * The list contents are of type {@link family.Person}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Has Uncle</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Has Uncle</em>' reference list.
	 * @see family.FamilyPackage#getPerson_HasUncle()
	 * @model
	 * @generated
	 */
	EList<Person> getHasUncle();

} // Person
