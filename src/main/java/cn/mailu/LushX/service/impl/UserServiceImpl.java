package cn.mailu.LushX.service.impl;

import cn.mailu.LushX.common.ServerResponse;
import cn.mailu.LushX.entity.User;
import cn.mailu.LushX.repository.UserRepository;
import cn.mailu.LushX.service.UserService;
import cn.mailu.LushX.util.MD5Util;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @Ahtuor: xuzhenya
 * @Description:
 * @Date: Created in 下午 9:00 2017-11-05
 * @Modified By:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void insert(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateById(User user) {
        deleteById(user.getUserId());
        userRepository.save(user);
    }

    @Override
    public void deleteById(String Id) {
        userRepository.delete(Id);
    }

    @Override
    public User selectById(String Id) {
        return userRepository.findOne(Id);
    }

    @Override
    public List<User> selectAll() {
        return userRepository.findAll();
    }

    @Override
    public ServerResponse<String> register(User user) {
        //验证是否已注册用户名
        User res=userRepository.findByUsername(user.getUsername());
        if(res!=null){
            return ServerResponse.createByErrorMessage("用户名已存在");
        }
        user.setRole("ROLE_USER");
        user.setUserId(UUID.randomUUID().toString());
        user.setMd5(MD5Util.MD5EncodeUtf8(user.getUsername()));
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        if(userRepository.save(user)==null){
            return ServerResponse.createByErrorMessage("注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

}
