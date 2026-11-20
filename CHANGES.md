# التعديلات على النسخة الأصلية

1. src/app/lab/page.tsx: إصلاح محاكي Statement coverage (إضافة السطر 2، فيصل إلى 100%).
2. src/app/api/action/route.ts: إجراء جديد quiz.attempts (إدراج مجمّع) وإجراء data.import (استيراد داخل transaction مع تنظيف المدخلات). الاستيراد يعكس ترتيب الجلسات والمحاولات والامتحانات قبل الإدراج حتى يبقى الأحدث بأكبر id.
3. src/lib/store.tsx: recordAttempts و importState.
4. src/app/practice/page.tsx: تسليم المحاكاة يرسل طلب الإجابات مرة واحدة بدل 40 طلباً.
5. src/components/BugChecker.tsx + src/app/skills/page.tsx: مدقّق تقرير العيب في قسم تقارير العيوب.
6. src/app/settings/page.tsx: زر استيراد نسخة JSON.

ملاحظة: drizzle.config.json يحتوي رابط قاعدة بيانات ثابتاً (app_db على 127.0.0.1)، عدّله لقاعدتك قبل تشغيل drizzle-kit push.

7. src/data/resources.ts: 13 مصدراً جديداً (ESTB مصر، عينات ISTQB الرسمية، مهارة-تك، مواقع تدريب عملي، نشرات ومجتمعات).
8. BugChecker: يفضّل سطر Title/العنوان إن وُجد بدل أول سطر.

9. src/data/resources.ts: إصلاح الروابط المعطلة/المدفوعة (testingeducation.org ← bbst.courses، مهارة-تك، ESTB)، حذف `istqb-samples` المكرر، ترقية uitestingplayground إلى https، وإضافة Testing بالعربي وSQA Dojo وISTQB ExpandTesting وBug Magnet وSECC.
10. src/data/career.ts: تحديث CERTS (CTFL-AT ← CTAL-AT v2.0) وخطوة الحجز b2 لمصر (ESTB/SECC).
11. src/lib/dates.ts: `timeZone: "UTC"` في dayLabel. src/app/progress/page.tsx: محاذاة علامة 65%. src/app/lab/page.tsx: منع تكرار قيم 2-value BVA. src/app/api/action/route.ts + src/app/settings/page.tsx: تحصين profile عند الاستيراد.

12. مصطلحات ISTQB بالإنجليزية بدل العربية: Defect/Failure/Test case/Static testing/Dynamic testing/Component testing/Acceptance criteria/Coverage/Defect report/stakeholders وغيرها في src/data/*.ts وصفحتي skills وlab وBugChecker؛ وأسماء الفصول والمواضيع صارت بصيغة «العربية (English)». أُزيل سطر العنوان الإنجليزي المكرر من صفحتي المنهج.
13. README.md: إضافة سياسة المصطلحات وجدولها، وإكمال خطوات التشغيل (قاعدة البيانات، DATABASE_URL، drizzle-kit push، الأوامر).

14. src/data/cards.ts: إصلاح تعريف بطاقة Defect الدائري («defect في work product…» ← «عيب أو خلل في work product…»).
15. src/data/syllabus.ts: إضافة العنوان الإنجليزي للمواضيع 1.4 و2.1 و4.3 لاتساق المواضيع الـ25؛ وحقل `short` اختياري للفصل 2 ودالة `chapterLabel()` للاسم المختصر في القوائم والجداول.
16. src/app/cards, certificate, progress, practice: استخدام `chapterLabel()` بدل `c.ar` في القوائم المنسدلة والجداول وأشرطة الدقة، و`max-w-full` على قائمة الفصول في صفحة البطاقات حتى لا تتجاوز عرض الشاشة. صفحتا المنهج تعرضان الاسم الكامل.
