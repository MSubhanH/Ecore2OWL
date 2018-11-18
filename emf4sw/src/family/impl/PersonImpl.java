/**
 */
package family.impl;

import family.FamilyPackage;
import family.Person;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Person</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link family.impl.PersonImpl#getName <em>Name</em>}</li>
 *   <li>{@link family.impl.PersonImpl#getHasFather <em>Has Father</em>}</li>
 *   <li>{@link family.impl.PersonImpl#getHasBrother <em>Has Brother</em>}</li>
 *   <li>{@link family.impl.PersonImpl#getHasUncle <em>Has Uncle</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PersonImpl extends MinimalEObjectImpl.Container implements Person {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getHasFather() <em>Has Father</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHasFather()
	 * @generated
	 * @ordered
	 */
	protected Person hasFather;

	/**
	 * The cached value of the '{@link #getHasBrother() <em>Has Brother</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHasBrother()
	 * @generated
	 * @ordered
	 */
	protected EList<Person> hasBrother;

	/**
	 * The cached value of the '{@link #getHasUncle() <em>Has Uncle</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHasUncle()
	 * @generated
	 * @ordered
	 */
	protected EList<Person> hasUncle;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PersonImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FamilyPackage.Literals.PERSON;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FamilyPackage.PERSON__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Person getHasFather() {
		if (hasFather != null && hasFather.eIsProxy()) {
			InternalEObject oldHasFather = (InternalEObject)hasFather;
			hasFather = (Person)eResolveProxy(oldHasFather);
			if (hasFather != oldHasFather) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FamilyPackage.PERSON__HAS_FATHER, oldHasFather, hasFather));
			}
		}
		return hasFather;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Person basicGetHasFather() {
		return hasFather;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHasFather(Person newHasFather) {
		Person oldHasFather = hasFather;
		hasFather = newHasFather;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FamilyPackage.PERSON__HAS_FATHER, oldHasFather, hasFather));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Person> getHasBrother() {
		if (hasBrother == null) {
			hasBrother = new EObjectResolvingEList<Person>(Person.class, this, FamilyPackage.PERSON__HAS_BROTHER);
		}
		return hasBrother;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Person> getHasUncle() {
		if (hasUncle == null) {
			hasUncle = new EObjectResolvingEList<Person>(Person.class, this, FamilyPackage.PERSON__HAS_UNCLE);
		}
		return hasUncle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FamilyPackage.PERSON__NAME:
				return getName();
			case FamilyPackage.PERSON__HAS_FATHER:
				if (resolve) return getHasFather();
				return basicGetHasFather();
			case FamilyPackage.PERSON__HAS_BROTHER:
				return getHasBrother();
			case FamilyPackage.PERSON__HAS_UNCLE:
				return getHasUncle();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case FamilyPackage.PERSON__NAME:
				setName((String)newValue);
				return;
			case FamilyPackage.PERSON__HAS_FATHER:
				setHasFather((Person)newValue);
				return;
			case FamilyPackage.PERSON__HAS_BROTHER:
				getHasBrother().clear();
				getHasBrother().addAll((Collection<? extends Person>)newValue);
				return;
			case FamilyPackage.PERSON__HAS_UNCLE:
				getHasUncle().clear();
				getHasUncle().addAll((Collection<? extends Person>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case FamilyPackage.PERSON__NAME:
				setName(NAME_EDEFAULT);
				return;
			case FamilyPackage.PERSON__HAS_FATHER:
				setHasFather((Person)null);
				return;
			case FamilyPackage.PERSON__HAS_BROTHER:
				getHasBrother().clear();
				return;
			case FamilyPackage.PERSON__HAS_UNCLE:
				getHasUncle().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case FamilyPackage.PERSON__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case FamilyPackage.PERSON__HAS_FATHER:
				return hasFather != null;
			case FamilyPackage.PERSON__HAS_BROTHER:
				return hasBrother != null && !hasBrother.isEmpty();
			case FamilyPackage.PERSON__HAS_UNCLE:
				return hasUncle != null && !hasUncle.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //PersonImpl
