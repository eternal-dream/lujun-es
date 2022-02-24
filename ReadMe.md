#Innocence_Springboot v1.0.0 (2020.07.13)
##一、项目技术栈
####
    springboot
    [mybatis-plus](https://baomidou.com/)
    shiro
    redis
##二、项目结构
####
    ├─sql                                   基础表sql
    └─src
      └─main
        ├─java
        │  └─com
        │      └─cqvip
        │          └─innocence
        │              ├─common             公共类，如下：缓存，session，工具等
        │              │  ├─cache
        │              │  ├─constant
        │              │  ├─sessions
        │              │  └─util
        │              ├─framework          配置相关类，如下：springboot配置类，过滤器，拦截器等
        │              │  ├─config
        │              │  ├─filter
        │              │  └─interceptor
        │              └─project            项目相关
        │                  ├─controller     controller
        │                  ├─mapper         mybatis-plus对应mapper接口
        │                  ├─model          实体
        │                  │  ├─dto         数据交互实体
        │                  │  └─entity      数据库对应实体
        │                  └─service        存放service接口，以及其实现
        │                      └─impl
        └─resources
            └─mybatis-plus
                └─mapper                    mybatis-plus的mapper对应的xml
##三、规范性要求
####
    （一）、数据库规范：
        1、无特殊要求的前提下，表名和字段名统一英文字母小写，并且采用下划线连接，各个连接词应该有实际的含义，如：
            *管理员角色关系表表名：admin_role;
            *管理员表的密码字段：pass_word;
            特殊情况：
                目前测试了国产数据库：人大金仓，由于该库对大小写敏感处理的问题，上面的小写规范不适用，如果使用
            该数据库，则表名和字段建议全部大写！！！
        2、中间的字段应采用主外键关联约束，并且外键字段要与对应的主表建立形式上的联系，如：
            *管理员角色关系表的字段：admin_id，role_id;
    （二）、包结构全部采用英文字母小写。
    （三）、controller层写法规范：
        1、类名上统一使用@RestController注解（可以在代码生成中设置rest风格注解）。
        2、禁止在controller写方法，以及过多的业务代码，方法全部写到service的实现类里面，业务复杂的，也在service的实现类里面处理；
        3、具体的方法接受请求时，尽量限定请求方式：post,get,patch,delete等。因此，建议使用对应的注解：
                @PostMapping，@GetMapping,@PatchMapping,@DeleteMapping.
            如果使用@RequestMapping，尽量指明请求类型，如：
                @RequestMapping(method = RequestMethod.GET,path = "/unauth");
    （四）、util的写法规范：
        1、所有的util放在uitl包下，根据不同的功能划分建立不同的子目录。
        2、所有的util向外暴露统一的newInstance方法,使用newInstance()获取方法实例。如：
            public class Md5Util {
                public static Md5Util newInstance() {
                    return (Md5Util)BaseUtil.newInstance(Md5Util.class.getName());
                }
                
                /**
                 * 其他方法。。。
                 */
            }
    （五）、model层的写法规范：
        1、所有数据库映射的实体类，全部放在entity包下。
        2、数据传输实体放在dto包下。
##四、代码生成器相关：
####
    代码生成器位置参考上面的项目结构图，目前只集成了针对postgresql的生成器。后续会根据实际情况继续集成如MySQL，orcale等。
    代码生成器生成的文件位置可以自行配置，如果生成的位置不符规范，请自行修改。
    1、因为配置了默认继承关系，所有与数据库映射的实体都会默认继承BaseModel以实现公共字段，如果不需要公共字段的，可以自行修改不
      继承BaseModel，但是必须自行实现Serializable接口。    
    2、由于设置了全局ID自动填充，所以当数据库内没有对应的"id"字段时，要手动设置实体类的某个字段为ID， 如下：

         * @TableId(type = IdType.INPUT)
        3、当实体字段与数据库列名不对应，并且既定的规范（驼峰，下划线）不能满足映射关系时，使用@TableField(value="数据库列名")指定对应，
                目前的代码生成其实会自动添加该注解（可配置不生成）。
        4、当数据库对应实体类存在的属性不在表中，但是必须使用时,推荐一下方法使mybatis-plus忽略该属性的字段对应。
            (1)、使用 @TableField(exist = false)，eg:
               @TableField(exist=false)
               private String noColumn;
            (2)、使用 transient 修饰，eg:
                private transient String noColumn;
        5、排除实体父类属性，eg:
            /**
             * 忽略父类 createTime 字段映射
             */
            private transient String createTime;
##五、XSS过滤器
####
        xss过滤器默认过滤了所有请求，如果某个请求不需要过滤，请在application.yml中配置，多个请求用英文“,”分隔，如下：
                `xss:
           excludes: *1/*,*2/*`    
##六、RSA非对称加密
####
        加密算法工具已经集成在common/util/encryption/RsaCodeUtil.java,需要使用时直接调用方法生成密钥对和公私钥，具体的加密场景按需引用。
        后续会进行前后端加密拦截的案列实现和封装，实现axios的请求加密。
##
##
##
##2021-08-16更新
####
    1、升级mybatis-plus至3.4.3
    2、升级mybatis-plus-generator至3.5.0
    3、删除MyDbtype,使用mybatis-plus自带的dbType
    4、修改代码生成器，调用方式不变
    5、新增达梦数据库代码生成工具
    6、因新版本的mybatis-plus-generator的生成文件路径配置未找到，所以生成的mapper.xml需要手动移动到resource下
##2021-07-09更新
####    
    修改全局日期转化，使其转化为LocalDateTime
##2021-06-30更新
####
    1、新增MariaDB代码生成器：MariaBbGenerator；
    2、实体对应的时间字段Date改用LocalDateTime,数据库对应数据类型为datetime。
    
##2021-06-28更新
####
    新增和更新设置立即刷新可见性，但是会影响性能，视业务情况按需使用,新增的代码如下：
        restTemplate.indexOps(t.getClass()).refresh();
        restTemplate.indexOps(IndexCoordinates.of(indexName)).refresh();
        UpdateQuery.builder(id).withRefresh(UpdateQuery.Refresh.Wait_For)
##2021-06-17更新
####
    删除原有的EsUtil,基于ElasticsearchRestTemplate和RestHighLevelClient对RestHighLevelClient（版本：7.9.2）的索引和文档操作进行基础封装：
    1、新增自定义注解：@DocumentId，作用于es数据对应实体的id字段上，一个实体只能有一个属性被用作文档id,且该字段属性必须为String;
    2、新增索引操作接口和实现：IndexService<T>和IndexServiceImpl<T>
    3、新增文档操作接口和实现：DocumentService<T>和DocumentServiceImpl<T>
    调用方式:
        使用Spring自动注入，注入时必须携带泛型！
        如：
             @Autowired
             private IndexService<EsTestInfo> indexService;
                
             @Autowired
             private DocumentService<EsTestInfo> documentService;
    示例：
        es文档对应实体示例：com.cqvip.innocence.project.model.entity.EsTestInfo;
        操作示例：com.cqvip.innocence.tests.EsTest;
##2021-06-11更新
####
      1、删除BaseDto
      2、删除BaseModel里面的分页字段
      3、重写AbstractController里面获取分页参数的方法，解耦，更符合程序设计思想，调用方式更简单合理，使返回的实体内容更简洁;
          继承了AbstractController的controller方法里面直接调用getPageParams()方法eg：
            public JsonResult getResources(String menuType, String name, Boolean sort){
                    Page page = getPageParams();
                    ......
            }
##2021-04-29更新
#### 
       AbstractController提供了获取分页的公共方法，controller继承AbstractController就可直接调用  
##2021-04-13更新
####
     将百度富文本编辑器的config.json移动到static下
     因为打成jar包会导致获取不到原来路径下的文件，因此将文件放到static下，保证了打包之后该配置文件在jar包外面，
     才能获取到。
     application.yml里面配置了存放config.json的位置，必须与实际存放地址保持一致！      
##2021-02-23更新
####    
        修改角色和权限资源编辑时，某个属性为null导致数据不更新该字段的问题；
        问题原因：
            mybatis-plus字段填充做了默认非空判断处理，如果某字段设置为null，update映射sql不会将null值设置进去。
            解决办法：
                1、在字段上添加注解：@TableField(fill = FieldFill.UPDATE)。不建议该方法，这样会导致其他sql执行时，如果没有设置
                该属性的值，会将原来的值设空！
                2、将传入的null值改为空串；实际测试得出：mybatis-plus默认会忽略null，但不会忽略空串。
                3、其他解决方案可参考mybatis-plus官方文档。
##2021-01-14更新
####
        ESUtil新增创建
           原有的createIndex(String index)方式,未对mapping进行设置，新增createIndex(Class clazz)方法,
           在相应的实体类class上添加@Document(indexName = "test")注解，在字段上添加
           @Field(analyzer = "ik_smart", type = FieldType.Text)注解，可创建相应indexName的索引，并对
           添加了@Field注解的字段设置对应配置。


                
##2021-01-06更新
####
    优化代码生成器，自定义注解指定生成器类型，然后通过枚举的数据库类型调用对应的生成器
    ```java
        @GeneratorType(dbType = MyDbType.POSTGRESQL)
        public class PostgresqlGenerator extends GeneratorService {}
    ```
    调用方式
    ```java
        GeneratorFactory factory = new GeneratorFactory();
        GeneratorService generator = factory.getGenerator(MyDbType.MYSQL);
        generator.generator();
    ```
                
##2020-11-25更新
####        
        ## 七、操作日志（张凌意）
        
        #### 自定义注解`@Log` 结合 SpringAOP实现日志操作
        
        **@Log**注解说明：
        
        ```java
        @Target({ElementType.PARAMETER, ElementType.METHOD})
        @Retention(RetentionPolicy.RUNTIME)
        @Documented
        public @interface Log {
            /**
             * 操作标题描述
             */
            public String title() default "";
        
            /**
             * 功能，默认BusinessType.OTHER
             */
            public BusinessType businessType() default BusinessType.OTHER;
        
            /**
             * 是否保存请求的参数，默认false
             */
            public boolean isSaveRequestData() default false;
        
            /**
             * 是否保存返回的结果，默认false
             */
            public boolean isSaveResponseData() default false;
        }
        ```
        
        使用方法如下：
        
        ```java
         	@Log(title = "获取用户信息",businessType = BusinessType.SELECT,isSaveRequestData = true,isSaveResponseData = true)
            @GetMapping("getInfo")
            @ApiOperation("获取当前登录管理员信息，包括角色，权限")
            public JsonResult getInfo(){
                //省略伪代码...
            }
        ```
        
        日志效果如下：



![image-20201125151246370](E:\doc\Typora_document\image-20201125151246370.png)

    关于mybatis-plus在多数据源情况下的问题：
        1、原生saveBatch方法不能使用，如有批量操作的需求，需自定义saveBatch方法

##2020-11-25更新
####
    ES检索新增聚合查询结果处理配置,原searchListData方法修改为searchData,返回结果由数据列表(List)修改为返回检索结果(SearchResponse),
    可从中获取数据列表、聚合查询结果(注意:由于聚合结果数据转JSON问题,在java中无法查看详细信息,可传入到前端页面显示)、总条数等信息。
##2020-10-28更新
####
     1、修改包结构，将代码生成器单独提出来。
     2、新增代码生成器工厂类，适配了postgresql和人大金仓数据库（Kingbase），调用方式如下：
             GeneratorFactory factory = new GeneratorFactory();
             GeneratorService geberator = factory.getGeberator(DbType.POSTGRESQL);
             geberator.generator();   

           

                



