package azaka7.artimancy.common.item;

public interface ISubtyped {
	
	public String getSubtypeLabel(int meta);

	/**
	 * The amount of sub-types available
	 */
	public int count();
	
}
