package com.onebucket.global.utils;

import com.onebucket.domain.universityManage.dto.verifiedCode.internal.VerifiedCodeDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * <br>package name   : com.onebucket.global.utils
 * <br>file name      : UniversityEmailValidator
 * <br>date           : 2024-07-05
 * <pre>
 * <span style="color: white;">[description]</span>
 * 클라이언트가 보낸 이메일이 해당 학교 이메일이 맞는지 검증한다.
 * </pre>
 * <pre>
 * <span style="color: white;">usage:</span>
 * {@code
 *  isValidUniversityEmail(String universityName, String email) : 해당 학교 학생인지 확인한다.
 * } </pre>
 * <pre>
 * modified log :
 * =======================================================
 * DATE           AUTHOR               NOTE
 * -------------------------------------------------------
 * 2024-07-05        SeungHoon              init create
 * </pre>
 */
@Component
public class UniversityEmailValidator {
    private static final Map<String, String> universityEmailDomains = new HashMap<>();
    private static final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$";
    static {
        universityEmailDomains.put("서울대학교", "snu.ac.kr");
        universityEmailDomains.put("연세대학교", "yonsei.ac.kr");
        universityEmailDomains.put("고려대학교", "korea.ac.kr");
        universityEmailDomains.put("서강대학교", "sogang.ac.kr");
        universityEmailDomains.put("성균관대학교", "skku.edu");
        universityEmailDomains.put("한양대학교", "hanyang.ac.kr");
        universityEmailDomains.put("중앙대학교", "cau.ac.kr");
        universityEmailDomains.put("경희대학교", "khu.ac.kr");
        universityEmailDomains.put("한국외국어대학교", "hufs.ac.kr");
        universityEmailDomains.put("서울시립대학교", "uos.ac.kr");
        universityEmailDomains.put("건국대학교", "konkuk.ac.kr");
        universityEmailDomains.put("동국대학교", "dgu.ac.kr");
        universityEmailDomains.put("홍익대학교", "hongik.ac.kr");
        universityEmailDomains.put("국민대학교", "kookmin.ac.kr");
        universityEmailDomains.put("숭실대학교", "soongsil.ac.kr");
        universityEmailDomains.put("세종대학교", "sju.ac.kr");
        universityEmailDomains.put("단국대학교", "dankook.ac.kr");
    }

    public boolean isValidUniversityEmail(VerifiedCodeDto dto) {
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);

        String domain = universityEmailDomains.get(dto.university());
        return pattern.matcher(dto.universityEmail()).matches() && dto.universityEmail().endsWith(domain);
    }
}