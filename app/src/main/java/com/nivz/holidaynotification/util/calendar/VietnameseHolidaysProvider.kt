package com.nivz.holidaynotification.util.calendar

import com.nivz.holidaynotification.data.model.Holiday

/**
 * Provides built-in Vietnamese holidays (solar and lunar)
 * Comprehensive list of official and traditional Vietnamese holidays
 * Including national holidays, commemorative days, and traditional observances
 */
object VietnameseHolidaysProvider {

    /**
     * Get all built-in Vietnamese holidays
     * Includes 23 solar calendar holidays and 10 lunar calendar holidays
     * Total: 33 holidays covering official public holidays and traditional observances
     */
    fun getVietnameseHolidays(): List<Holiday> {
        val holidays = mutableListOf<Holiday>()

        // ============== SOLAR CALENDAR HOLIDAYS (DƯƠNG LỊCH) - 23 DAYS ==============

        // January
        holidays.add(Holiday(
            name = "Tết Dương Lịch",
            description = "Ngày đầu năm mới Dương lịch, ngày lễ quốc tế",
            day = 1,
            month = 1,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            notificationHour = 0,
            colorCode = "#2196F3"
        ))

        holidays.add(Holiday(
            name = "Ngày Sinh Viên Việt Nam",
            description = "Tưởng niệm Trần Văn Ơn và vinh danh học sinh, sinh viên Việt Nam",
            day = 9,
            month = 1,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#FF6E40"
        ))

        // February
        holidays.add(Holiday(
            name = "Ngày Thành Lập Đảng",
            description = "Kỷ niệm ngày thành lập Đảng Cộng sản Việt Nam (1930)",
            day = 3,
            month = 2,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#D32F2F"
        ))

        holidays.add(Holiday(
            name = "Ngày Thầy Thuốc Việt Nam",
            description = "Vinh danh đội ngũ y bác sĩ, xuất phát từ thư Bác Hồ (1955)",
            day = 27,
            month = 2,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#4CAF50"
        ))

        // March
        holidays.add(Holiday(
            name = "Ngày Quốc Tế Phụ Nữ",
            description = "Ngày tôn vinh phụ nữ, tưởng nhớ Hai Bà Trưng",
            day = 8,
            month = 3,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#E91E63"
        ))

        holidays.add(Holiday(
            name = "Ngày Thành Lập Đoàn Thanh Niên",
            description = "Kỷ niệm ngày thành lập Đoàn Thanh niên Cộng sản Hồ Chí Minh (1931)",
            day = 26,
            month = 3,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#9C27B0"
        ))

        // April
        holidays.add(Holiday(
            name = "Ngày Sách Việt Nam",
            description = "Khuyến khích đọc sách, kỷ niệm xuất bản 'Đường Kách Mệnh' (1927)",
            day = 21,
            month = 4,
            isLunar = false,
            isCustom = false,
            notificationEnabled = false,
            colorCode = "#FFB300"
        ))

        holidays.add(Holiday(
            name = "Ngày Kiến Trúc Sư Việt Nam",
            description = "Tôn vinh giới kiến trúc, thành lập Hội Kiến trúc sư Việt Nam (1948)",
            day = 27,
            month = 4,
            isLunar = false,
            isCustom = false,
            notificationEnabled = false,
            colorCode = "#607D8B"
        ))

        holidays.add(Holiday(
            name = "Ngày Giải Phóng Miền Nam",
            description = "Kỷ niệm ngày thống nhất đất nước (1975)",
            day = 30,
            month = 4,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 1,
            notificationHour = 0,
            colorCode = "#F44336"
        ))

        // May
        holidays.add(Holiday(
            name = "Ngày Quốc Tế Lao Động",
            description = "Ngày của người lao động trên toàn thế giới",
            day = 1,
            month = 5,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            notificationHour = 0,
            colorCode = "#FF5722"
        ))

        holidays.add(Holiday(
            name = "Ngày Chiến Thắng Điện Biên Phủ",
            description = "Kỷ niệm chiến thắng lịch sử năm 1954 chống thực dân Pháp",
            day = 7,
            month = 5,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#D32F2F"
        ))

        holidays.add(Holiday(
            name = "Ngày Chiến Thắng Phát Xít",
            description = "Kỷ niệm kết thúc Chiến tranh thế giới thứ hai tại châu Âu",
            day = 9,
            month = 5,
            isLunar = false,
            isCustom = false,
            notificationEnabled = false,
            colorCode = "#1976D2"
        ))

        holidays.add(Holiday(
            name = "Ngày Sinh Chủ Tịch Hồ Chí Minh",
            description = "Kỷ niệm ngày sinh Chủ tịch Hồ Chí Minh (1890)",
            day = 19,
            month = 5,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#D32F2F"
        ))

        // June
        holidays.add(Holiday(
            name = "Ngày Quốc Tế Thiếu Nhi",
            description = "Ngày dành cho trẻ em trên toàn thế giới",
            day = 1,
            month = 6,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#FF6E40"
        ))

        holidays.add(Holiday(
            name = "Ngày Gia Đình Việt Nam",
            description = "Tôn vinh giá trị gia đình Việt Nam",
            day = 28,
            month = 6,
            isLunar = false,
            isCustom = false,
            notificationEnabled = false,
            colorCode = "#4CAF50"
        ))

        // July
        holidays.add(Holiday(
            name = "Ngày Thương Binh Liệt Sĩ",
            description = "Tưởng nhớ và tri ân các anh hùng liệt sĩ, thương binh",
            day = 27,
            month = 7,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#D32F2F"
        ))

        // August
        holidays.add(Holiday(
            name = "Ngày Cách Mạng Tháng Tám",
            description = "Kỷ niệm thắng lợi Cách mạng Tháng Tám (1945)",
            day = 19,
            month = 8,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#D32F2F"
        ))

        // September
        holidays.add(Holiday(
            name = "Quốc Khánh Việt Nam",
            description = "Kỷ niệm ngày Chủ tịch Hồ Chí Minh đọc Tuyên ngôn Độc lập (1945)",
            day = 2,
            month = 9,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            notificationHour = 0,
            colorCode = "#F44336"
        ))

        // October
        holidays.add(Holiday(
            name = "Ngày Giải Phóng Thủ Đô",
            description = "Kỷ niệm ngày Thủ đô Hà Nội được giải phóng (1954)",
            day = 10,
            month = 10,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#D32F2F"
        ))

        holidays.add(Holiday(
            name = "Ngày Doanh Nhân Việt Nam",
            description = "Tôn vinh giới doanh nhân Việt Nam",
            day = 13,
            month = 10,
            isLunar = false,
            isCustom = false,
            notificationEnabled = false,
            colorCode = "#FF9800"
        ))

        holidays.add(Holiday(
            name = "Ngày Phụ Nữ Việt Nam",
            description = "Kỷ niệm ngày thành lập Hội Liên hiệp Phụ nữ Việt Nam (1930)",
            day = 20,
            month = 10,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#E91E63"
        ))

        // November
        holidays.add(Holiday(
            name = "Ngày Nhà Giáo Việt Nam",
            description = "Tôn vinh những người làm công tác giáo dục",
            day = 20,
            month = 11,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#8BC34A"
        ))

        // December
        holidays.add(Holiday(
            name = "Ngày Thành Lập Quân Đội Nhân Dân Việt Nam",
            description = "Ngày Quốc phòng toàn dân, kỷ niệm thành lập Quân đội Nhân dân Việt Nam (1944)",
            day = 22,
            month = 12,
            isLunar = false,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#D32F2F"
        ))

        // ============== LUNAR CALENDAR HOLIDAYS (ÂM LỊCH) - 10 DAYS ==============

        // Lunar New Year Period
        holidays.add(Holiday(
            name = "Tết Nguyên Đán",
            description = "Từ mùng 2 đến mùng 5 tháng 1 âm lịch - Ngày lễ lớn nhất trong năm, chào đón năm mới âm lịch, sum họp gia đình",
            day = 1,
            month = 1,
            isLunar = true,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            notificationHour = 0,
            colorCode = "#FF1744"
        ))

        holidays.add(Holiday(
            name = "Tết Nguyên Tiêu",
            description = "Rằm tháng Giêng (ngày 15/1 âm lịch) - Lễ hội cầu an đầu năm",
            day = 15,
            month = 1,
            isLunar = true,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#FDD835"
        ))

        // Second lunar month
        holidays.add(Holiday(
            name = "Giỗ Tổ Hùng Vương",
            description = "Lễ tưởng niệm các Vua Hùng, tổ tiên dân tộc Việt Nam (được công nhận từ 2007)",
            day = 10,
            month = 3,
            isLunar = true,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#4CAF50"
        ))

        holidays.add(Holiday(
            name = "Tết Hàn Thực",
            description = "Lễ tưởng niệm Giới Tử Thôi, ăn bánh trôi bánh chay",
            day = 3,
            month = 3,
            isLunar = true,
            isCustom = false,
            notificationEnabled = false,
            colorCode = "#A1887F"
        ))

        // Fourth lunar month
        holidays.add(Holiday(
            name = "Lễ Phật Đản",
            description = "Kỷ niệm ngày sinh, thành đạo và nhập Niết Bàn của Đức Phật Thích Ca",
            day = 15,
            month = 4,
            isLunar = true,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#9C27B0"
        ))

        // Fifth lunar month
        holidays.add(Holiday(
            name = "Tết Đoan Ngọ",
            description = "Lễ diệt sâu bọ, cầu mùa màng tốt tươi",
            day = 5,
            month = 5,
            isLunar = true,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#2196F3"
        ))

        // Seventh lunar month
        holidays.add(Holiday(
            name = "Lễ Vu Lan",
            description = "Rằm tháng Bảy - Lễ báo hiếu cha mẹ và tưởng nhớ tổ tiên",
            day = 15,
            month = 7,
            isLunar = true,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            colorCode = "#B39DDB"
        ))

        // Eighth lunar month
        holidays.add(Holiday(
            name = "Ngày Giỗ Tổ Sân Khấu",
            description = "Tôn vinh các bậc tiền nhân trong nghệ thuật sân khấu",
            day = 12,
            month = 8,
            isLunar = true,
            isCustom = false,
            notificationEnabled = false,
            colorCode = "#FF6E40"
        ))

        holidays.add(Holiday(
            name = "Tết Trung Thu",
            description = "Lễ hội trăng rằm, dành cho thiếu nhi và mừng mùa thu hoạch",
            day = 15,
            month = 8,
            isLunar = true,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            notificationHour = 18,
            colorCode = "#FDD835"
        ))

        // Twelfth lunar month
        holidays.add(Holiday(
            name = "Lễ Ông Công Ông Táo",
            description = "Lễ tiễn Táo Quân chầu trời, báo cáo Ngọc Hoàng, đánh dấu kết thúc năm âm lịch",
            day = 23,
            month = 12,
            isLunar = true,
            isCustom = false,
            notificationEnabled = true,
            notificationDays = 0,
            notificationHour = 16,
            colorCode = "#FF5252"
        ))

        return holidays
    }

    /**
     * Get Vietnamese holidays by month (solar calendar)
     */
    fun getHolidaysByMonth(month: Int): List<Holiday> {
        return getVietnameseHolidays().filter { it.month == month && !it.isLunar }
    }

    /**
     * Get Vietnamese lunar holidays by lunar month
     */
    fun getLunarHolidaysByMonth(month: Int): List<Holiday> {
        return getVietnameseHolidays().filter { it.month == month && it.isLunar }
    }

    /**
     * Get holiday name for a specific solar date
     */
    fun getHolidayName(day: Int, month: Int): String? {
        return getVietnameseHolidays().find {
            it.day == day && it.month == month && !it.isLunar
        }?.name
    }

    /**
     * Get holiday name for a specific lunar date
     */
    fun getLunarHolidayName(day: Int, month: Int): String? {
        return getVietnameseHolidays().find {
            it.day == day && it.month == month && it.isLunar
        }?.name
    }
}
