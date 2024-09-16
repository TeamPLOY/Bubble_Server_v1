package com.laundering.laundering_server.domain.member.model.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WashingRoom {
    B41("B41", "B", 401, 417, 0, 0),    // "B41" 세탁실에 해당하는 B401~B417 범위
    B42("B42", "B", 418, 434, 0, 0),    // "B42" 세탁실에 해당하는 B418~B434 범위
    B31("B31", "B", 302, 307, 327, 334), // "B31" 세탁실에 해당하는 B302~B307 및 B327~B334 범위
    B32("B32", "B", 308, 326, 0, 0),    // "B32" 세탁실에 해당하는 B308~B326 범위
    UNKNOWN("UNKNOWN", "", 0, 0, 0, 0); // 범위 밖일 때

    private final String washingRoom; // 세탁실 이름 (예: B41, A31)
    private final String prefix;      // 방 번호의 앞자리 (예: "A" 또는 "B")
    private final int minRoom;        // 방 번호의 최소 값 (범위 시작)
    private final int maxRoom;        // 방 번호의 최대 값 (범위 끝)
    private final int minRoomExtra;   // 추가 범위의 최소 값 (선택적, 사용 안 할 경우 0)
    private final int maxRoomExtra;   // 추가 범위의 최대 값 (선택적, 사용 안 할 경우 0)

    // prefix(방 번호 앞자리)와 roomNumber(방 번호)를 기반으로 해당하는 세탁실을 찾아 반환
    public static String findWashingRoom(String prefix, int roomNumber) {
        // 모든 Enum 값을 순회하면서 조건에 맞는 세탁실을 찾음
        for (WashingRoom room : values()) {
            // 기본 범위 또는 추가 범위 내에 있을 경우 해당 세탁실 이름 반환
            if (room.prefix.equals(prefix) &&
                    ((room.minRoom <= roomNumber && room.maxRoom >= roomNumber) ||
                            (room.minRoomExtra <= roomNumber && room.maxRoomExtra >= roomNumber))) {
                return room.washingRoom;
            }
        }
        // 조건에 맞는 세탁실이 없을 경우 "UNKNOWN" 반환
        return UNKNOWN.washingRoom;
    }
}
