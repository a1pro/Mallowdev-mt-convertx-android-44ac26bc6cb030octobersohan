package com.mallowtech.adapter;

/**
 * The Class CheckBoxState.
 */
public class CheckBoxState {

	/** The from name. */
	String fromName = null;
	
	/** The to name. */
	String toName = null;
	
	/** The primary id. */
	Integer primaryId = 0;

	/** The selected. */
	boolean selected = false;

	/**
	 * Instantiates a new check box state.
	 *
	 * @param fromName the from name
	 * @param toName the to name
	 * @param selected the selected
	 * @param integer the integer
	 */
	public CheckBoxState(String fromName, String toName, boolean selected, Integer integer) {
		super();
		this.fromName = fromName;
		this.toName = toName;
		this.selected = selected;
		this.primaryId = integer;
	}

	/**
	 * Gets the primary id.
	 *
	 * @return the primary id
	 */
	public int getPrimaryID() {
		return primaryId;
	}

	/**
	 * Sets the primary id.
	 *
	 * @param primaryID the new primary id
	 */
	public void setPrimaryID(int primaryID) {
		this.primaryId = primaryID;
	}

	/**
	 * Gets the from name.
	 *
	 * @return the from name
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * Sets the from name.
	 *
	 * @param fromName the new from name
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * Gets the to name.
	 *
	 * @return the to name
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * Sets the to name.
	 *
	 * @param toName the new to name
	 */
	public void setToName(String toName) {
		this.toName = toName;
	}

	/**
	 * Checks if is selected.
	 *
	 * @return true, if is selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets the selected.
	 *
	 * @param selected the new selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
