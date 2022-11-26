package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
public interface RefreshTokenRepository {
    void addRefreshToken (@Param("refreshToken") String refreshToken);
}
