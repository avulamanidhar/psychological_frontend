/**
 * SECTION 2 – FOCUS TIMER
 * Slide 0: Focus Mode (25:00)
 * Slide 1: Break Mode (05:00)
 */
import BottomNav from './BottomNav.jsx'

export default function FocusTimer({ currentSlide, onGoToSlide, onPrevious }) {
  const isFocus = currentSlide === 0

  return (
    <div className="bg-bg-splash min-h-screen flex flex-col">
      <div className="flex-1 px-6 pt-7">
        <button type="button" className="text-desc-gray text-[16px] font-medium" onClick={onPrevious}>
          ‹ Back
        </button>

        <h1 className="text-title-blue text-[34px] font-extrabold mt-7 leading-none">Focus Timer</h1>
        <p className="text-desc-gray text-[14px] mt-2">Pomodoro technique for deep focus</p>

        <div className="flex justify-center items-center gap-8 mt-10">
          <button
            type="button"
            onClick={() => onGoToSlide?.(0)}
            className={isFocus ? 'px-8 py-3 rounded-2xl bg-button-blue text-white flex items-center gap-2 shadow-[0_10px_20px_rgba(74,144,226,0.25)]' : 'flex items-center gap-2 text-desc-gray'}
          >
            <span className="text-[16px]">🎯</span>
            <span className={isFocus ? 'text-[15px] font-bold' : 'text-[15px] font-semibold'}>Focus</span>
          </button>
          <button
            type="button"
            onClick={() => onGoToSlide?.(1)}
            className={!isFocus ? 'px-8 py-3 rounded-2xl bg-button-blue text-white flex items-center gap-2 shadow-[0_10px_20px_rgba(74,144,226,0.25)]' : 'flex items-center gap-2 text-desc-gray'}
          >
            <span className="text-[16px]">☕</span>
            <span className={!isFocus ? 'text-[15px] font-bold' : 'text-[15px] font-semibold'}>Break</span>
          </button>
        </div>

        <div className="flex flex-col items-center mt-12">
          <div className="relative w-[240px] h-[240px] flex items-center justify-center">
            <div className="absolute inset-0 rounded-full border-[14px] border-stroke-gray/40" />
            <div className="absolute inset-[20px] rounded-full bg-white/40" />
            <div className="relative z-10 text-center">
              <div className="text-title-blue text-[44px] font-extrabold tracking-tight">
                {isFocus ? '25:00' : '05:00'}
              </div>
              <div className="text-desc-gray text-[14px] mt-1">
                {isFocus ? 'Focus Time' : 'Break Time'}
              </div>
            </div>
          </div>

          <div className="flex items-center gap-3 mt-8 text-desc-gray">
            <div className="flex gap-2">
              {[1, 2, 3, 4].map((i) => (
                <div key={i} className="w-2 h-2 rounded-full bg-stroke-gray/60" />
              ))}
            </div>
            <div className="text-[14px]">0/4 sessions</div>
          </div>

          <div className="flex items-center gap-10 mt-9">
            <button type="button" className="w-[74px] h-[74px] rounded-full bg-button-blue text-white flex items-center justify-center shadow-[0_14px_28px_rgba(74,144,226,0.35)]">
              <span className="text-[22px] ml-1">▶</span>
            </button>
            <button type="button" className="w-[74px] h-[74px] rounded-full bg-white/70 border border-stroke-gray/70 text-desc-gray flex items-center justify-center">
              <span className="text-[26px]">↻</span>
            </button>
          </div>
        </div>
      </div>

      <BottomNav />
    </div>
  )
}
