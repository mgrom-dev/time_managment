Домашнее задание: 
1. Повторить код с урока
2. Запустить Eureka, Запустить 2 части (rest + page) 
3. В качестве результата работы прислать 1 скриншот со страницы localhost:8761, 
где видно оба запущенных приложения (Instances currently registered with Eureka)
4. **** Попробовать запустить несколько экземпляров (instances) timesheets-rest
5. **** Поизучать про RestTemplate. Попробовать завести его с использованием аннотации LoadBalanced


```java
class User {
//  @ManyToMany(fetch = FetchType.EAGER)
//  @JoinTable(
//    name = "users_roles",
//    joinColumns = @JoinColumn(name = "user_id"),
//    inverseJoinColumns = @JoinColumn(name = "role_id")
//  )
//  private List<Role> roles;
}

class UserRole {
  @Id
  Long id;
  @ManyToOne
  User user;
  @ManyToOne
  Role role;
}

@Data
class Role {
//@ToString.Exclude
//@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
//private List<User> users;
} 
```