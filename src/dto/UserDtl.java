package dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDtl extends User{
	private int usercode;
	private int money;
}