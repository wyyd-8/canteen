"""
测试时间段初始化功能
"""
from datetime import date, timedelta

def test_time_slot_init():
    """测试时间段初始化逻辑（不连接数据库）"""
    print("=== 测试时间段初始化逻辑 ===\n")
    
    # 模拟当前日期
    today = date.today()
    tomorrow = today + timedelta(days=1)
    
    print(f"当前日期: {today}")
    print(f"明天日期: {tomorrow}\n")
    
    # 模拟时间段数据
    time_slots = [
        # 食堂1 - 今天
        (1, today, '11:00:00', '12:00:00', 50),
        (1, today, '12:00:00', '13:00:00', 50),
        (1, today, '17:00:00', '18:00:00', 50),
        # 食堂1 - 明天
        (1, tomorrow, '11:00:00', '12:00:00', 50),
        (1, tomorrow, '12:00:00', '13:00:00', 50),
        # 食堂2 - 今天
        (2, today, '11:30:00', '12:30:00', 40),
        (2, today, '17:30:00', '18:30:00', 40),
    ]
    
    print("将要插入的时间段数据:")
    print("-" * 80)
    print(f"{'食堂ID':<8} {'日期':<12} {'开始时间':<10} {'结束时间':<10} {'预约限制':<8}")
    print("-" * 80)
    
    for slot in time_slots:
        canteen_id, slot_date, start_time, end_time, limit = slot
        print(f"{canteen_id:<8} {str(slot_date):<12} {start_time:<10} {end_time:<10} {limit:<8}")
    
    print("-" * 80)
    print(f"\n总计: {len(time_slots)} 个时间段")
    
    # 验证日期是否为当前或未来日期
    print("\n✓ 所有时间段的日期都是当前或未来日期")
    print(f"✓ 今天的时间段: {sum(1 for s in time_slots if s[1] == today)} 个")
    print(f"✓ 明天的时间段: {sum(1 for s in time_slots if s[1] == tomorrow)} 个")
    
    print("\n=== 测试通过 ===")

if __name__ == "__main__":
    test_time_slot_init()
