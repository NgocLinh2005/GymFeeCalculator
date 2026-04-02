/**
 * Bài toán: Tính phí đăng ký tập Gym
 */
import java.util.Scanner;
public class GymFeeCalculator {

    // ---------------------------------------------------------------
    // Hằng số đơn giá theo loại gói (VNĐ/tháng)
    // ---------------------------------------------------------------
    private static final long DON_GIA_CO_BAN   = 300000;
    private static final long DON_GIA_NANG_CAO = 500000;
    private static final long DON_GIA_VIP       = 900000;

    /**
     * Hàm chính tính phí đăng ký tập gym.
     *
     * @param loaiGoi    loại gói (1=Cơ bản, 2=Nâng cao, 3=VIP)
     * @param soThang    số tháng đăng ký [1..12]
     * @param doTuoi     tuổi khách hàng [15..60]
     * @param coSinhVien có thẻ sinh viên (0=không, 1=có)
     * @return phí cần thanh toán (VNĐ) hoặc -1 nếu đầu vào không hợp lệ
     */
    public static long tinhPhiDangKy(int loaiGoi, int soThang, int doTuoi, int coSinhVien) {

        // --- Bước 1: Kiểm tra đầu vào ---
        if (!isValidLoaiGoi(loaiGoi))       return -1;
        if (!isValidSoThang(soThang))       return -1;
        if (!isValidDoTuoi(doTuoi))         return -1;
        if (!isValidCoSinhVien(coSinhVien)) return -1;

    // --- Bước 2: Tính giá gốc ---
        long donGia = getDonGia(loaiGoi);
        double giaGoc = (double) donGia * soThang;

    // --- Bước 3: Hệ số ưu đãi theo số tháng ---
        double heSoThang = getHeSoThang(soThang);

    // --- Bước 4: Tỉ lệ giảm giá theo tuổi ---
        double giamTuoi = getGiamTuoi(doTuoi);

    // --- Bước 5: Tỉ lệ giảm giá sinh viên (có ràng buộc chéo với doTuoi) ---
        double giamSinhVien = getGiamSinhVien(coSinhVien, doTuoi);

    // --- Bước 6: Tính phí cuối, làm tròn xuống nghìn đồng ---
        double phiThuc = giaGoc * heSoThang * (1 - giamTuoi) * (1 - giamSinhVien);
        return lamTronNghinDong(phiThuc);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== CHUONG TRINH TINH PHI TAP GYM ===");

        try {
            // 1. Nhập loại gói
            System.out.println("Chon loai goi (1: Co ban, 2: Nang cao, 3: VIP): ");
            int loaiGoi = sc.nextInt();

            // 2. Nhập số tháng
            System.out.println("Nhap so thang dang ky (1-12): ");
            int soThang = sc.nextInt();

            // 3. Nhập độ tuổi
            System.out.println("Nhap do tuoi cua ban (15-60): ");
            int doTuoi = sc.nextInt();

            // 4. Nhập trạng thái sinh viên
            System.out.println("Ban co the sinh vien khong? (1: Co, 0: Khong): ");
            int coSinhVien = sc.nextInt();

            // Gọi hàm xử lý logic
            long ketQua = tinhPhiDangKy(loaiGoi, soThang, doTuoi, coSinhVien);

            // In kết quả
            System.out.println("------------------------------------");
            if (ketQua == -1) {
                System.out.println("Loi: Thong tin nhap vao khong hop le!");
            } else {
                // Hiển thị số tiền
                System.out.println("Tong phi can thanh toan: " + ketQua + " VND");
            }
            System.out.println("------------------------------------");

        } catch (Exception e) {
            System.out.println("Loi: Vui long chi nhap so nguyen!");
        } finally {
            sc.close();
        }
    }
    // ---------------------------------------------------------------
    // Các hàm kiểm tra đầu vào
    // ---------------------------------------------------------------

    private static boolean isValidLoaiGoi(int loaiGoi) {
        return loaiGoi >= 1 && loaiGoi <= 3;
    }

    private static boolean isValidSoThang(int soThang) {
        return soThang >= 1 && soThang <= 12;
    }

    private static boolean isValidDoTuoi(int doTuoi) {
        return doTuoi >= 15 && doTuoi <= 60;
    }

    private static boolean isValidCoSinhVien(int coSinhVien) {
        return coSinhVien == 0 || coSinhVien == 1;
    }

    // ---------------------------------------------------------------
    // Các hàm tính toán chi tiết
    // ---------------------------------------------------------------

    private static long getDonGia(int loaiGoi) {
        switch (loaiGoi) {
            case 1: return DON_GIA_CO_BAN;
            case 2: return DON_GIA_NANG_CAO;
            case 3: return DON_GIA_VIP;
            default: return 0;
        }
    }

    private static double getHeSoThang(int soThang) {
        if (soThang <= 2)  return 1.0;
        if (soThang <= 5)  return 0.95;
        return                     0.85;
    }

    private static double getGiamTuoi(int doTuoi) {
        if (doTuoi <= 17) return 0.10;
        if (doTuoi <= 55) return 0.0;
        return                  0.15;
    }

    private static double getGiamSinhVien(int coSinhVien, int doTuoi) {
        if (coSinhVien == 1 && doTuoi >= 18 && doTuoi <= 25) {
            return 0.10;
        }
        return 0.0;
    }

    private static long lamTronNghinDong(double soTien) {
        return (long) (soTien / 1000) * 1000;
    }
}