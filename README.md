# SQLite-and-Room

## DB in Android
- Khi phát triển ứng dụng Android, ta sẽ gặp những case phải lưu trữ dữ liệu ở đâu đó, Một db bạn tạo bạn tạo có thể rất tuyệt vời hoặc nó cũng làm việc phát triển tiếp theo của ứng dụng khó khăn hơn bh hết. Trong Android có 2 loại db phổ biến là Realm với SQLite

## Realm 
- Realm là một cơ sở dữ liệu nhẹ, có thể thay thế cả hai thư viện SQL và ORM trong các ứng dụng Android. Realm không sử dụng SQLite làm engine của nó. Thay vào đó, nó dùng core C++ nhằm mục đích cung cấp một thư viện cơ sở dữ liệu thay thế SQLite.
Realm lưu trữ dữ liệu trong các bảng viết bằng core C++. Việc này cho phép Realm được truy cập dữ liệu từ nhiều ngôn ngữ cũng như một loạt các truy vấn khác nhau.
### Những ưu điểm của Realm so với SQLite:
- Nhanh hơn so với SQLite (gấp 10 lần so với SQLite cho các hoạt động bình thường).
- Dễ sử dụng.
- Chuyển đổi đối tượng xử lý cho bạn.
- Thuận tiện cho việc tạo ra và lưu trữ dữ liệu nhanh chóng.
### Ngoài ra còn có một số nhược điểm sau :
- Vẫn còn đang phát triển.
- Không có nhiều kênh trao đổi trực tuyến.
- Không thể truy cập các đối tượng thông qua thread.

- Realm hỗ trợ đa nền tảng (hiện tại là Android, iOS, OSX), file CSDL có thể chia sẻ dễ dàng giữa các nền tảng trên. Realmluôn giữ tư tưởng nâng cao hiệu năng và giữ vững độ ổn định. Kết quả benchmark (có source code) cho thấy Realm nhanh hơn khoảng 2-10 lần trong các tác vụ đọc, ghi so với SQLitethuần và một số thư viện ORM phổ biến hiện nay.

### Cách thức lưu dữ liệu của Realm
- Realm tổ chức lưu dữ liệu dưới dạng cây B-Tree
- Lý do chính cho sự tồn tại của B-Trees là sử dụng tốt hơn hành vi của các thiết bị đọc và ghi các khối dữ liệu lớn. Hai thuộc tính quan trọng để làm cho cây B tốt hơn cây nhị phân khi dữ liệu phải được lưu trữ trên đĩa:

- Truy cập vào đĩa thực sự chậm (so với bộ nhớ hoặc cache, truy cập ngẫu nhiên vào dữ liệu trên đĩa là các đơn đặt hàng có cường độ chậm hơn)
- Mỗi lần đọc đơn lẻ làm cho toàn bộ khu vực được nạp từ ổ đĩa - giả sử kích thước sector là 4K, điều này có nghĩa là 1000 số nguyên hoặc hàng chục đối tượng lớn hơn bạn đang lưu trữ.
- Do đó, chúng ta có thể sử dụng những ưu điểm của thực tế thứ hai, đồng thời cũng giảm thiểu số lượng truy cập đĩa khuyết điểm.

- Vì vậy, thay vì chỉ lưu trữ một số trong mỗi nút cho chúng ta biết nếu chúng ta nên tiếp tục sang trái hoặc sang phải, chúng ta có thể tạo chỉ mục lớn hơn cho chúng ta biết liệu chúng ta có nên tiếp tục 1/100 đầu tiên hay không hoặc đến ngày thứ 99 (hãy tưởng tượng sách trong thư viện được sắp xếp theo chữ cái đầu tiên của chúng, sau đó là chữ cái thứ hai, v.v.). 

- Điều này dẫn đến gần như logb N tra cứu, trong đó N là số lượng bản ghi. Con số này, trong khi tiệm cận giống như log2 N, thực sự nhỏ hơn vài lần với N và b đủ lớn và vì chúng ta đang nói về lưu trữ dữ liệu vào đĩa để sử dụng trong cơ sở dữ liệu, v.v ..., lượng dữ liệu thường lớn đủ để biện minh cho điều này.

- Phần còn lại của quyết định thiết kế chủ yếu được thực hiện để làm cho một trong những hoạt động hiệu quả, như sửa đổi một cây N-ary là phức tạp hơn một nhị phân.

- Cây B-tree là cây tìm kiếm nhị phân. Cây B có thể có nhiều hơn hai nút con. Trong thực tế, số lượng các nút con là biến.

- Vì vậy, bạn có thể thay đổi số lượng các nút con sao cho kích thước của một nút luôn là bội số của kích thước khối hệ thống tệp. Điều này làm giảm lãng phí khi đọc: bạn không thể đọc ít hơn một khối anyway, bạn luôn phải đọc toàn bộ khối, vì vậy bạn cũng có thể điền vào nó với dữ liệu hữu ích. Việc tăng số lượng các nút con cũng sẽ làm giảm độ sâu của cây, do đó giảm số lượng trung bình của "hoa bia" (tức là đĩa đọc), một lần nữa tăng hiệu suất.

- Lưu ý: B Tree thường được sử dụng để lưu trữ các cấu trúc dữ liệu có độ lớn lớn hơn bộ nhớ, trong khi các cây nhị phân thường được sử dụng để lưu trữ các cấu trúc dữ liệu có đơn đặt hàng có độ lớn nhỏ hơn bộ nhớ. Trong thực tế, B- tree được thiết kế đặc biệt như một cấu trúc dữ liệu trên đĩa như trái ngược với cấu trúc dữ liệu trong bộ nhớ.

### Realm hay SQLite
- Tùy vào bài toán tùy nhu cầu, tùy khách hàng
- Realm sử dụng cú pháp lưu đơn gian, query nhanh với những dữ liệu lớn, nhưng các query phức tạp có vẻ khó khăn, nếu có nhiều quan hệ sql có vẻ tốt hơn
- Realm phải import thư viện nó sẽ làm app tăng lên vài MB còn SQLite thì ko 
- Sqlite code khá dài và phức tạp, nhưng khi Room ra mắt thì khá ngon

## SQL 
- Lưu trữ dữ liệu vào database là ý tưởng dành cho các dữ liệu kiểu lặp lại hoặc có cấu trúc , ví dụ như thông tin danh bạ.
- APIs mà chúng ta sẽ dùng để sử dụng database trên Android có trong package sau : 
	Android.database.sqlite
-  Sẽ không có sự kiểm tra tại compile-time đối với các truy vẫn SQL. Khi cấu trúc data thay đổi bạn phải tự cập nhật các truy vấn này thủ công. 
- Bạn sẽ phải sử dụng nhiều code boilerplate để chuyển đổi giữa các SQL queries và data object
### Schema and contact
- Schema là một trong những nguyên tắc chính của db : nó là một khai báo về cách mà dữ liệu sẽ được tổ chức.

- Một contract class là container cho các hằng số sẽ định nghĩa tên cho URIs, tables, và column.Class này cho phép bạn sử dụng các thông tin trên trong tất cả các class thuộc cùng package.Và khi sử đổi chúng ta cũng chỉ cần thay đổi tại 1 nơi trong project.

- Cách tốt để tạo class này là sẽ đưa các định nghĩa global với db vào root-level của class.Sau đó ta sẽ tạo ra các iner-class tương ứng với các table cụ thể.

          public final class FeedReaderContract {
          // To prevent someone from accidentally instantiating the contract class,
          // make the constructor private.
          private FeedReaderContract() {}

          /* Inner class that defines the table contents */
          public static class FeedEntry implements BaseColumns {
              public static final String TABLE_NAME = "entry";
              public static final String COLUMN_NAME_TITLE = "title";
              public static final String COLUMN_NAME_SUBTITLE = "subtitle";
          }
      }
      
### Create DB
- Sau khi đã định nghĩa được db của bạn sẽ như thế nào, bây giờ chúng ta sẽ tạo ra db cùng các table của nó.
- Tạo statement để tạo hoặc xóa bảng :

          private static final String SQL_CREATE_ENTRIES =
          "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
          FeedEntry._ID + " INTEGER PRIMARY KEY," +
          FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
          FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

      private static final String SQL_DELETE_ENTRIES =
          "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

- Sau đó dùng SQLiteOpenHelper để quản lý db.Khi dùng class này để tham chiếu đến db của bạn , hệ thống sẽ chỉ thực hiện những hoạt động mất thời gian với tạo và cập nhật db khi cần thiết, và không làm nó khi app khởi động.

- CHúng ta sẽ tạo subclass của nó và override  onCreate() và onUpgrade() callback methods. Bạn cũng có thể override  onDowngrade() hoặc  onOpen() methods, nhưng nó không phải là bắt buộc.

            public class FeedReaderDbHelper extends SQLiteOpenHelper {
                // If you change the database schema, you must increment the database version.
                public static final int DATABASE_VERSION = 1;
                public static final String DATABASE_NAME = "FeedReader.db";

                public FeedReaderDbHelper(Context context) {
                    super(context, DATABASE_NAME, null, DATABASE_VERSION);
                }
                public void onCreate(SQLiteDatabase db) {
                    db.execSQL(SQL_CREATE_ENTRIES);
                }
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    // This database is only a cache for online data, so its upgrade policy is
                    // to simply to discard the data and start over
                    db.execSQL(SQL_DELETE_ENTRIES);
                    onCreate(db);
                }
                public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                    onUpgrade(db, oldVersion, newVersion);
                }
            }
   
- Khởi tạo đối tượng helper để truy cập db : 
         
         FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getContext());

###  Các thao tác cơ bản với db 

 - Thêm data : Truyền ContentValues object cho insert() method:
 	
		// Gets the data repository in write mode
		SQLiteDatabase db = mDbHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_NAME_TITLE, title);
		values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

		// Insert the new row, returning the primary key value of the new row
		long newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);

- Tham số thứ 2 : định nghĩa cách mà hệ thống sẽ thực hiện khi tham số values thứ 3 mà bạn thêm vào null. Trong trường hợp này, khi tham số thứ 2 để là null, thì hệ thống sẽ không thêm row mới khi value null. CÒn nếu tham số thứ 2 là tên cột thì hệ thống vẫn thêm row mới với giá trị thuộc tên cột đó là null.

- Method này trả về id của row mới, hoặc -1 nếu có lỗi xảy ra.

- Đọc data

		SQLiteDatabase db = mDbHelper.getReadableDatabase();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
		    BaseColumns._ID,
		    FeedEntry.COLUMN_NAME_TITLE,
		    FeedEntry.COLUMN_NAME_SUBTITLE
		    };

		// Filter results WHERE "title" = 'My Title'
		String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
		String[] selectionArgs = { "My Title" };

		// How you want the results sorted in the resulting Cursor
		String sortOrder =
		    FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

		Cursor cursor = db.query(
		    FeedEntry.TABLE_NAME,   // The table to query
		    projection,             // The array of columns to return (pass null to get all)
		    selection,              // The columns for the WHERE clause
		    selectionArgs,          // The values for the WHERE clause
		    null,                   // don't group the rows
		    null,                   // don't filter by row groups
		    sortOrder               // The sort order
		    );

- Đocj kết quả : 

		List itemIds = new ArrayList<>();
		while(cursor.moveToNext()) {
		  long itemId = cursor.getLong(
		      cursor.getColumnIndexOrThrow(FeedEntry._ID));
		  itemIds.add(itemId);
		}
		cursor.close();
		
- Xóa data

		// Define 'where' part of query.
		String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { "MyTitle" };
		// Issue SQL statement.
		int deletedRows = db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);

- Cập nhật Db

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		// New value for one column
		String title = "MyNewTitle";
		ContentValues values = new ContentValues();
		values.put(FeedEntry.COLUMN_NAME_TITLE, title);

		// Which row to update, based on the title
		String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
		String[] selectionArgs = { "MyOldTitle" };

		int count = db.update(
		    FeedReaderDbHelper.FeedEntry.TABLE_NAME,
		    values,
		    selection,
		    selectionArgs);
		    
## Room
- Room  là một Persistence Library cung cấp một lớp trừu tượng trên SQLite để cho phép truy cập cơ sở dữ liệu dễ dàng trong khi khai thác toàn bộ sức mạnh của SQLite.

### Đặc điểm:
- Framework chính (Sqlite Database) cung cấp các built-in support cho các trường hợp làm việc với các nội dung SQL thô. Mặc dù các API này khá mạnh mẽ nhưng chúng lại tương đối low-level và yêu cầu khá nhiều thời gian và nỗ lực để sử dụng:

- Không có xác thực các câu truy vấn SQL ở thời điểm compile-time. Khi data graph thay đổi thì dev sẽ phải cập nhật lại các câu truy vấn SQL thủ công. Việc này khá mất thời gian và xác suất gặp lỗi trong quá trình khá lớn.

- Sẽ phải dùng nhiều code khung để chuyển đổi giữa truy vấn SQL với các Java data object (Phần này chắc ai làm việc với DB nhiều chắc chắn hiểu rõ)

- Có 3 thành phần chính trong Room

- Database: Chứa database holder đóng vai trò là điểm truy cập chính cho kết nối cơ bản với dữ liệu quan hệ. Annotation sẽ cung cấp danh sách các thực thể và nội dung class sẽ định nghĩa danh sách các DAO (đối tượng truy cập CSDL) của CSDL. Nó cũng là điểm truy cập chính cho các kết nối phía dưới. Annotated class nên để là lớp abstract extends RoomDatabase. Tại thời điểm runtime thì dev có thể nhận được một instance của nó bằng cách gọi Room.databaseBuilder() hoặc Room.inMemoryDatabaseBuilder(). 

- Entity: Đại diện cho 1 bảng cơ sở dữ liệu

- DAO: Chứa các phương thức được sử dụng để truy cập cơ sở dữ liệu.

- Ứng dụng sử dụng cơ sở dữ liệu Room để lấy các đối tượng truy cập dữ liệu hoặc DAO, được liên kết với cơ sở dữ liệu đó. Sau đó, ứng dụng sử dụng mỗi DAO để nhận các thực thể từ cơ sở dữ liệu và lưu mọi thay đổi đối với các thực thể đó trở lại cơ sở dữ liệu. Cuối cùng, ứng dụng sử dụng một thực thể để lấy và đặt các giá trị tương ứng với các cột trong cơ sở dữ liệu.

### Define data
- Khi sử dụng Room, bạn xác định các trường liên quan là entities. Với mỗi entity, một bảng đc tạo trong đối tượng database được liên kết với các item. Ta phải tham chiếu lớp entity qua mảng thực tế trong Database class

- User 
		
		@Entity
		data class User(
		    @PrimaryKey var id: Int,
		    var firstName: String?,
		    var lastName: String?
		)
		
- Để persitst 1 trường, Room phải có quyền truy cập vào nó. Ta có thể đặt trường công khai hoặc ta có thể cung cấp 1 getter và setter cho nó. 

-  Lưu ý: Các entity có thể có một hàm tạo trống (nếu lớp DAO tương ứng có thể truy cập từng trường tồn tại) hoặc một hàm tạo có tham số chứa các kiểu và tên khớp với các trường trong thực thể. Room cũng có thể sử dụng các hàm tạo đầy đủ hoặc một phần, chẳng hạn như hàm tạo chỉ nhận được một số trường.

### @PrimaryKey

- Mỗi entity phải xác định ít nhất 1 trường là khóa chính. Ngay cả khi chỉ có 1 trường, bạn vẫn cần chú thích trường với 
#PrimaryKey. Ngoài ra, nếu bạn muốn Room gán ID tự động cho các thực thể, bạn có thể đặt thuộc tính autoGenerate của 
PrimaryKey. 

		@Entity(primaryKeys = arrayOf("firstName", "lastName"))
		data class User(
		    var firstName: String?,
		    var lastName: String?
		)

- Mặc định, room sử dụng tên lớp làm tên bảng cơ sở dữ liệu, nếu muốn bảng có 1 tên khác, hãy đặt thuộc tính tên bảng với anitotion @Entity:		
		
		@Entity(tableName = "users")
		data class User (
		    @PrimaryKey var id: Int,
		    @ColumnInfo(name = "first_name") var firstName: String?,
		    @ColumnInfo(name = "last_name") var lastName: String?
		)
		
### Ignore fields

- Mặc định, Room tạo 1 cột cho từng trường được xác định trong entity. Nếu 1 thực thể có các trường mà ko muốn tồn tại có thể sử dụng 
@Ignore 
		
		@Entity
		data class User(
		    @PrimaryKey var id: Int,
		    var firstName: String?,
		    var lastName: String?,
		    @Ignore var picture: Bitmap?
		)
		
- Trong trường hợp một thực thể kế thừa các trường từ một thực thể khác, việc sử dụng thuộc tính bị bỏ qua của thuộc tính @Entity thường dễ dàng hơn:

		open class User {
		    var picture: Bitmap? = null
		}

		@Entity(ignoredColumns = arrayOf("picture"))
		data class RemoteUser(
		    @PrimaryKey var id: Int,
		    var hasVpn: Boolean
		) : User()
		
### Provide table search support







      

