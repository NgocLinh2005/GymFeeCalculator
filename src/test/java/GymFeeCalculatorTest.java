import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GymFeeCalculatorTest {

    @ParameterizedTest(name = "Lần {index}: Goi={0}, Tháng={1}, Tuổi={2}, SV={3} => Kỳ vọng={4}")
    @DisplayName("Kiểm thử giá trị biên cho hệ thống tính phí Gym")
    @CsvSource({
            // --- Kiểm thử biên ---

            // --- Kiểm thử loaiGoi (Biên 1, 2, 3) ---
            "0, 6, 30, 0, -1",          // Sai: Gói < 1
            "1, 6, 30, 0, 1530000",     // Đúng: 300k * 6 * 0.85
            "2, 6, 30, 0, 2550000",     // Đúng: 500k * 6 * 0.85
            "3, 6, 30, 0, 4590000",     // Đúng: 900k * 6 * 0.85
            "4, 6, 30, 0, -1",          // Sai: Gói > 3

            // --- Kiểm thử soThang (Biên 1, 12 và các mốc giảm giá) ---
            "2, 0, 30, 0, -1",          // Sai: Tháng < 1
            "2, 1, 30, 0, 500000",      // Đúng: 1 tháng (hệ số 1.0)
            "2, 2, 30, 0, 1000000",     // Đúng: 2 tháng (hệ số 1.0)
            "2, 7, 30, 0, 2975000",     // Đúng: 7 tháng (hệ số 0.85)
            "2, 11, 30, 0, 4675000",    // Đúng: 11 tháng (hệ số 0.85)
            "2, 12, 30, 0, 5100000",    // Đúng: 12 tháng (hệ số 0.85)
            "2, 13, 30, 0, -1",         // Sai: Tháng > 12

            // --- Kiểm thử doTuoi (Biên 15, 17, 18, 55, 56, 60) ---
            "2, 6, 14, 0, -1",          // Sai: Tuổi < 15
            "2, 6, 15, 0, 2295000",     // Đúng: 15 tuổi (giảm 10%)
            "2, 6, 16, 0, 2295000",     // Đúng: 16 tuổi (giảm 10%)
            "2, 6, 17, 0, 2295000",     // Đúng: 17 tuổi (giảm 10%)
            "2, 6, 18, 0, 2550000",     // Đúng: 18 tuổi (không giảm tuổi)
            "2, 6, 25, 0, 2550000",     // Đúng: 25 tuổi (không giảm tuổi)
            "2, 6, 26, 0, 2550000",     // Đúng: 26 tuổi (không giảm tuổi)
            "2, 6, 55, 0, 2550000",     // Đúng: 55 tuổi (không giảm tuổi)
            "2, 6, 56, 0, 2167000",     // Đúng: 56 tuổi (giảm 15% - làm tròn xuống)
            "2, 6, 59, 0, 2167000",     // Đúng: 59 tuổi (giảm 15% - làm tròn xuống)
            "2, 6, 60, 0, 2167000",     // Đúng: 60 tuổi (giảm 15% - làm tròn xuống)
            "2, 6, 61, 0, -1",          // Sai: Tuổi > 60

            // --- Kiểm thử coSinhVien ---
            "2, 6, 30, -1, -1",         // Sai: Giá trị không hợp lệ
            "2, 6, 30, 0, 2550000",     // Đúng: Không có thẻ SV
            "2, 6, 30, 1, 2550000",     // Đúng: Có thẻ SV nhưng tuổi 30 (không được giảm)
            "2, 6, 30, 2, -1",           // Sai: Giá trị không hợp lệ

            //---Kiêm thử bảng quyết định
            "1, 1, 20, 0, 300000",   // Gói 1, 1 tháng, ko giảm
            "1, 4, 20, 0, 1140000",  // Gói 1, 4 tháng (0.95)
            "1, 8, 22, 1, 1836000",  // Gói 1, 8 tháng (0.85), SV (0.9)

            "2, 1, 22, 1, 450000",   // Gói 2, 1 tháng, SV (giảm 10%)
            "2, 4, 58, 0, 1615000",  // Gói 2, 4 tháng (0.95), Già (0.15)
            "2, 6, 22, 1, 2295000",  // Gói 2, 6 tháng (0.85), SV (0.1)
            "3, 1, 20, 0, 900000",   // Gói 3, 1 tháng, ko giảm
            "3, 1, 16, 0, 810000",   // Gói 3, 1 tháng, Trẻ (0.1)
            "3, 3, 20, 0, 2565000",  // Gói 3, 3 tháng (0.95)
            "3, 4, 58, 0, 2907000",  // Gói 3, 4 tháng (0.95), Già (0.15)
            "3, 8, 22, 1, 5508000",  // Gói 3, 8 tháng (0.85), SV (0.1)

            // --- CÁC TRƯỜNG HỢP LỖI (Dữ liệu không hợp lệ cuối bảng) ---
            "3, 13, 20, 0, -1",      // Tháng 13 (quá hạn)
            "0, 6, 30, 0, -1",       // Gói 0 (ko tồn tại)
            "2, 0, 30, 0, -1",       // Tháng 0
            "2, 6, 14, 0, -1",       // 14 tuổi (quá trẻ)
            "2, 6, 30, 2, -1"        // SV = 2 (sai định dạng)
    })
    void testTinhPhiGym(int loaiGoi, int soThang, int doTuoi, int coSinhVien, long expected) {
        long actual = GymFeeCalculator.tinhPhiDangKy(loaiGoi, soThang, doTuoi, coSinhVien);
        assertEquals(expected, actual,
                String.format("Lỗi tại bộ: %d, %d, %d, %d", loaiGoi, soThang, doTuoi, coSinhVien));
    }
}