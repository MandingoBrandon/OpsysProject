

public class Status
{
	public static final Status UNDEF = new Status(-1);
	
	public static final Status EMPTY = new Status(0);
	
	public static final Status CPU = new Status(1);
	
	public static final Status IO = new Status(2);
	
	public static final Status ARRIVING = new Status(3);
	
	public static final Status CTX_SWITCH = new Status(4);
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Status)
		{
			Status status = (Status) obj;
			return this.id == status.id;
		}
		return false;
	}
	
	public int compareTo(Status status)
	{
		return this.id - status.id;
	}
	
	private Status(final int id)
	{
		this.id = id;
	}
	
	private final int id;
}
